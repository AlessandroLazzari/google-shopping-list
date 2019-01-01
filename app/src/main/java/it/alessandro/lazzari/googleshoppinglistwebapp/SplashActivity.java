package it.alessandro.lazzari.googleshoppinglistwebapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String LOGCAT = SplashActivity.class.getSimpleName();

    private static Boolean mIsValid = Boolean.FALSE;
    private static SplashActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        mIsValid = Boolean.TRUE;

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public static void finishSplash() {
        if (mIsValid && mActivity != null) {
            mActivity.finish();
            mIsValid = Boolean.FALSE;
        }
    }
}