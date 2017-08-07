package com.dd.template.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.template.BuildConfig;
import com.dd.template.R;

import static com.dd.template.utils.ViewUtils.findById;

public class MainActivity extends AppCompatActivity implements MainView {
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button exitView = findById(this, R.id.exitView);
        exitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onExitClicked();
            }
        });

        showImeInfo();

        final EditText editText = findById(this, R.id.editView);
        Button goView = findById(this, R.id.goView);
        goView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                String s = editText.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    intent.putExtra("param", s);
                }
                startActivity(intent);
            }
        });

        presenter = new MainPresenter(this);
    }

    public void showImeInfo() {
        TextView textView = findById(this, R.id.imeInfo);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        StringBuffer info = new StringBuffer();
        for (InputMethodInfo imi: imm.getEnabledInputMethodList()) {
            info.append(imi.getServiceName()).append("\n");
        }
        textView.setText(info.toString());
    }

    @Override
    public void exit() {
        finish();
    }
}
