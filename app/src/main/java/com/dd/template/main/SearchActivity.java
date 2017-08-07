package com.dd.template.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextClock;
import android.widget.Toast;

import com.dd.template.R;

import static com.dd.template.utils.ViewUtils.findById;

/**
 * Created by yangfeng on 17/8/7.
 *
 * Present android crash error http://www.jianshu.com/p/4c5fafe08fa7
 * android.view.WindowManager$BadTokenException: 1 - 3, the last 4 type occur with
 * ActivityGroup, was not present here.
 */

public class SearchActivity extends AppCompatActivity {
    private TextClock clockView;
    private PopupWindow popupWindow;

    private String param;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        clockView = findById(this, R.id.clockView);
        popupWindow = new PopupWindow(View.inflate(this, R.layout.activity_search, null), 500, 400, false);

        param = getIntent().getStringExtra("param");

        if ("1".equals(param)) {
            // trigger error "android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?"
            tryShowPopup();
        } else if ("11".equals(param)) {
            postShowPopup();
        } else if ("2".equals(param)) {
            // trigger error "android.view.WindowManager$BadTokenException: Unable to add window -- token null is not for an application"
            tryShowAlert(true);
        } else if ("3".equals(param) || "33".equals(param)) {
            // trigger error "android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@XXXXX is not valid; is your activity running?"
            postFinish(3000);
        } else {
            tryShowAlert(false);
        }

        clockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryShowAlert(true);
            }
        });
    }

    private void postShowPopup() {
        clockView.postDelayed(new Runnable() {
            @Override
            public void run() {
                tryShowPopup();
            }
        }, 0);
    }

    private void postFinish(int timeout) {
        clockView.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                // MUST post in another runnable, or 'android.view.WindowLeaked:' error occur instead
                postShowAlert(500);
            }
        }, timeout);
    }

    private void postShowAlert(int timeout) {
        clockView.postDelayed(new Runnable() {
            @Override
            public void run() {
                tryShowAlert(false);
            }
        }, timeout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // error 'android.view.WindowLeaked:' occurs if there is any popup without being dismissed.
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private void tryShowPopup() {
        popupWindow.showAtLocation(clockView, Gravity.BOTTOM, 0, 0);
    }

    private void tryShowAlert(boolean error) {
        final Context context;
        if (error) {
            context = getApplicationContext();
        } else {
            if ("33".equals(param) && isFinishing()) {
                Toast.makeText(this, "Activity is finished, not show alert anymore", Toast.LENGTH_LONG).show();
                return;
            }
            context = this;
        }

        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Warnning")
                .create().show();
    }
}
