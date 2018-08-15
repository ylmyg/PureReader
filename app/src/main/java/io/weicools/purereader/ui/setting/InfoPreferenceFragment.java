package io.weicools.purereader.ui.setting;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import io.weicools.purereader.AppConfig;
import io.weicools.purereader.BuildConfig;
import io.weicools.purereader.R;
import io.weicools.purereader.ui.CustomTabsHelper;
import io.weicools.purereader.ui.about.AboutActivity;

/**
 * @author Weicools Create on 2018.04.13
 *
 * desc:
 */
public class InfoPreferenceFragment extends PreferenceFragmentCompat {

  private static final int MSG_GLIDE_CLEAR_CACHE_DONE = 1;

  private final Handler handler = new Handler(msg -> {
    switch (msg.what) {
      case MSG_GLIDE_CLEAR_CACHE_DONE:
        showMessage(R.string.clear_image_cache_successfully);
        break;
      default:
        break;
    }
    return true;
  });

  @Override
  public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.info_preference);

    // Setting of night mode
    findPreference(AppConfig.KEY_NIGHT_MODE).setOnPreferenceChangeListener((preference, newValue) -> {
      if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
          == Configuration.UI_MODE_NIGHT_YES) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
      } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
      }
      getActivity().getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
      getActivity().recreate();
      return true;
    });

    // Clear the cache of glide
    findPreference("clear_glide_cache").setOnPreferenceClickListener(preference -> {
      new Thread(() -> {
        Glide.get(getContext()).clearDiskCache();
        handler.sendEmptyMessage(MSG_GLIDE_CLEAR_CACHE_DONE);
      }).start();
      return true;
    });

    // set Version
    findPreference("version").setTitle(getString(R.string.version) + BuildConfig.VERSION_NAME);

    // Open the github contributors page
    findPreference("contributors").setOnPreferenceClickListener(preference -> {
      startActivity(new Intent(getContext(), AboutActivity.class));
      // CustomTabsHelper.openUrl(getContext(), getString(R.string.author_page_desc));
      return true;
    });

    // Open the github links
    findPreference("follow_me_on_github").setOnPreferenceClickListener(preference -> {
      CustomTabsHelper.openUrl(getContext(), getString(R.string.follow_me_on_github_desc));
      return true;
    });

    // Feedback through sending an email
    findPreference("feedback").setOnPreferenceClickListener(preference -> {
      try {
        Uri uri = Uri.parse(getString(R.string.sendto));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.device_model)
            + Build.MODEL
            + "\n"
            + getString(R.string.sdk_version)
            + Build.VERSION.RELEASE
            + "\n"
            + getString(R.string.version));
        startActivity(intent);
      } catch (android.content.ActivityNotFoundException ex) {
        showMessage(R.string.no_mail_app);
      }
      return true;
    });

    // Open the github home page
    findPreference("source_code").setOnPreferenceClickListener(preference -> {
      CustomTabsHelper.openUrl(getContext(), getString(R.string.source_code_desc));
      return true;
    });

    // Show the open source licenses
    findPreference("open_source_license").setOnPreferenceClickListener(preference -> {
      startActivity(new Intent(getActivity(), LicenseActivity.class));
      return true;
    });
  }

  private void showMessage (@StringRes int resId) {
    Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
  }
}
