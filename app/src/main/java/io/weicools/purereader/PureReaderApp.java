package io.weicools.purereader;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import io.weicools.purereader.util.ToastUtil;

/**
 * @author Weicools Create on 2017/12/2.
 * <p>
 * Initialize some properties in the application such as the app theme.
 */
public class PureReaderApp extends Application {
  private static Context sAppInstance;

  @Override
  public void onCreate () {
    super.onCreate();
    sAppInstance = this;
    ToastUtil.init(this);
    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("night_mode", false)) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
  }

  public static Context getAppInstance () {
    return sAppInstance;
  }
}
