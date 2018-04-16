package io.weicools.purereader.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Create by weicools on 2018/4/16.
 * <p>
 * desc:
 */

public class ToastUtil {
    @SuppressLint("StaticFieldLeak")
    private static Application application;


    public static void init(Application application) {
        ToastUtil.application = application;
    }


    public static void showShort(String text) {
        Toast.makeText(application, text, Toast.LENGTH_SHORT).show();
    }


    public static void showShort(@StringRes int resId) {
        showShort(application.getString(resId));
    }


    public static void showLong(String text) {
        Toast.makeText(application, text, Toast.LENGTH_LONG).show();
    }


    public static void showLong(@StringRes int resId) {
        showLong(application.getString(resId));
    }
}
