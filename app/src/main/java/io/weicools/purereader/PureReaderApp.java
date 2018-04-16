package io.weicools.purereader;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.weicools.purereader.util.ToastUtil;

/**
 * Create by weicools on 2017/12/2.
 * <p>
 * Initialize some properties in the application such as the app theme.
 */

public class PureReaderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        Fresco.initialize(this);
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("night_mode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
