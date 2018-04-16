package io.weicools.purereader.ui.setting;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import io.weicools.purereader.AppConfig;
import io.weicools.purereader.BuildConfig;
import io.weicools.purereader.R;
import io.weicools.purereader.ui.CustomTabsHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoPreferenceFragment extends PreferenceFragmentCompat {

    private static final int MSG_GLIDE_CLEAR_CACHE_DONE = 1;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GLIDE_CLEAR_CACHE_DONE:
                    showMessage(R.string.clear_image_cache_successfully);
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.info_preference);

        // Setting of night mode
        findPreference(AppConfig.KEY_NIGHT_MODE).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                        == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                getActivity().getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                getActivity().recreate();
                return true;
            }
        });

        // Clear the cache of glide
        findPreference("clear_glide_cache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getContext()).clearDiskCache();
                        handler.sendEmptyMessage(MSG_GLIDE_CLEAR_CACHE_DONE);
                    }
                }).start();
                return true;
            }
        });

        // set Version
        findPreference("version").setTitle(getString(R.string.version) + BuildConfig.VERSION_NAME);

        // Rate
//        findPreference("rate").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                try {
//                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    showMessage(R.string.something_wrong);
//                }
//                return true;
//            }
//        });

        // Open the github contributors page
        findPreference("contributors").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomTabsHelper.openUrl(getContext(), getString(R.string.author_page_desc));
                return true;
            }
        });

        // Open the github links
        findPreference("follow_me_on_github").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomTabsHelper.openUrl(getContext(), getString(R.string.follow_me_on_github_desc));
                return true;
            }
        });

        // Feedback through sending an email
        findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse(getString(R.string.sendto));
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic));
                    intent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.device_model) + Build.MODEL + "\n"
                                    + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                                    + getString(R.string.version));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    showMessage(R.string.no_mail_app);
                }
                return true;
            }
        });

        // Open the github home page
        findPreference("source_code").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomTabsHelper.openUrl(getContext(), getString(R.string.source_code_desc));
                return true;
            }
        });

        // Show the open source licenses
        findPreference("open_source_license").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), LicenseActivity.class));
                return true;
            }
        });
    }

    private void showMessage(@StringRes int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
