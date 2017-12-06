package io.weicools.purereader.ui.ui_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import io.weicools.purereader.R;
import io.weicools.purereader.service.CacheService;
import io.weicools.purereader.ui.favorite.FavoriteFragment;
import io.weicools.purereader.ui.setting.SettingFragment;

/**
 * Created by Weicools on 2017/12/2.
 *
 * Main UI for displaying the {@link ViewPager}
 * which was set up with {@link TabLayout}.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID = "KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID";
    public static final String ACTION_FAVORITE = "io.weicools.purereader.favorite";

    private ZhihuDailyFragmentV2 mZhihuDailyFragment;
    private FavoriteFragment mFavoriteFragment;
    private SettingFragment mSettingFragment;

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.nav_bottom);

        initFragment(savedInstanceState);

        // FIXME: 2017/12/2 new FavoritePresenter

        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID, R.id.nav_timeline);
            switch (id) {
                case R.id.nav_timeline:
                    showFragment(mZhihuDailyFragment);
                    break;
                case R.id.nav_favorite:
                    showFragment(mFavoriteFragment);
                    break;
                case R.id.nav_setting:
                    showFragment(mSettingFragment);
                    break;
                default:
                    break;
            }
        } else {
            Log.e(TAG, "savedInstanceState is null");
            if (TextUtils.equals(getIntent().getAction(), ACTION_FAVORITE)) {
                showFragment(mFavoriteFragment);
                mBottomNavigationView.setSelectedItemId(R.id.nav_favorite);
            } else {
                showFragment(mZhihuDailyFragment);
            }
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.nav_timeline:
                        showFragment(mZhihuDailyFragment);
                        break;

                    case R.id.nav_favorite:
                        showFragment(mFavoriteFragment);
                        break;

                    case R.id.nav_setting:
                        showFragment(mSettingFragment);
                        break;

                    default:
                        break;

                }
                ft.commit();
                return true;
            }
        });

        startService(new Intent(MainActivity.this, CacheService.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID, mBottomNavigationView.getSelectedItemId());
        FragmentManager fm = getSupportFragmentManager();
        if (mZhihuDailyFragment.isAdded()) {
            fm.putFragment(outState, ZhihuDailyFragmentV2.class.getSimpleName(), mZhihuDailyFragment);
        }
        if (mFavoriteFragment.isAdded()) {
            fm.putFragment(outState, FavoriteFragment.class.getSimpleName(), mFavoriteFragment);
        }
        if (mSettingFragment.isAdded()) {
            fm.putFragment(outState, SettingFragment.class.getSimpleName(), mSettingFragment);
        }
    }

    private void initFragment(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mZhihuDailyFragment = ZhihuDailyFragmentV2.newInstance();
            mFavoriteFragment = FavoriteFragment.newInstance();
            mSettingFragment = SettingFragment.newInstance();
        } else {
            mZhihuDailyFragment = (ZhihuDailyFragmentV2) fm.getFragment(savedInstanceState, ZhihuDailyFragmentV2.class.getSimpleName());
            mFavoriteFragment = (FavoriteFragment) fm.getFragment(savedInstanceState, FavoriteFragment.class.getSimpleName());
            mSettingFragment = (SettingFragment) fm.getFragment(savedInstanceState, SettingFragment.class.getSimpleName());
        }

        if (!mZhihuDailyFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.main_container, mZhihuDailyFragment, ZhihuDailyFragmentV2.class.getSimpleName())
                    .commit();
        }

        if (!mFavoriteFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.main_container, mFavoriteFragment, FavoriteFragment.class.getSimpleName())
                    .commit();
        }

        if (!mSettingFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.main_container, mSettingFragment, SettingFragment.class.getSimpleName())
                    .commit();
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        if (fragment instanceof ZhihuDailyFragmentV2) {
            fm.beginTransaction()
                    .show(mZhihuDailyFragment)
                    .hide(mFavoriteFragment)
                    .hide(mSettingFragment)
                    .commit();
        } else if (fragment instanceof FavoriteFragment) {
            fm.beginTransaction()
                    .hide(mZhihuDailyFragment)
                    .show(mFavoriteFragment)
                    .hide(mSettingFragment)
                    .commit();
        } else if (fragment instanceof SettingFragment) {
            fm.beginTransaction()
                    .hide(mZhihuDailyFragment)
                    .hide(mFavoriteFragment)
                    .show(mSettingFragment)
                    .commit();
        }
    }
}
