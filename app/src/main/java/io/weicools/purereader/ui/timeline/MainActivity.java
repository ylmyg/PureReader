package io.weicools.purereader.ui.timeline;

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
import io.weicools.purereader.ui.meizi.GirlsFragment;
import io.weicools.purereader.service.CacheService;
import io.weicools.purereader.ui.favorite.FavoriteFragment;
import io.weicools.purereader.ui.setting.MyInfoFragment;
import io.weicools.purereader.util.BottomNavigationViewHelper;

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

    private TimelineFragment mTimelineFragment;
    private GirlsFragment mGirlsFragment;
    private FavoriteFragment mFavoriteFragment;
    private MyInfoFragment mMyInfoFragment;

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

        initFragment(savedInstanceState);

        // FIXME: 2017/12/2 new FavoritePresenter

        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID, R.id.nav_timeline);
            switch (id) {
                case R.id.nav_timeline:
                    showFragment(mTimelineFragment);
                    break;
                case R.id.nav_girls:
                    showFragment(mGirlsFragment);
                    break;
                case R.id.nav_favorite:
                    showFragment(mFavoriteFragment);
                    break;
                case R.id.nav_setting:
                    showFragment(mMyInfoFragment);
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
                showFragment(mTimelineFragment);
            }
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.nav_timeline:
                        showFragment(mTimelineFragment);
                        break;
                    case R.id.nav_girls:
                        showFragment(mGirlsFragment);
                        break;

                    case R.id.nav_favorite:
                        showFragment(mFavoriteFragment);
                        break;

                    case R.id.nav_setting:
                        showFragment(mMyInfoFragment);
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
        if (mTimelineFragment.isAdded()) {
            fm.putFragment(outState, TimelineFragment.class.getSimpleName(), mTimelineFragment);
        }
        if (mGirlsFragment.isAdded()) {
            fm.putFragment(outState, GirlsFragment.class.getSimpleName(), mGirlsFragment);
        }
        if (mFavoriteFragment.isAdded()) {
            fm.putFragment(outState, FavoriteFragment.class.getSimpleName(), mFavoriteFragment);
        }
        if (mMyInfoFragment.isAdded()) {
            fm.putFragment(outState, MyInfoFragment.class.getSimpleName(), mMyInfoFragment);
        }
    }

    private void initFragment(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mTimelineFragment = TimelineFragment.newInstance();
            mGirlsFragment = GirlsFragment.newInstance();
            mFavoriteFragment = FavoriteFragment.newInstance();
            mMyInfoFragment = MyInfoFragment.newInstance();
        } else {
            mTimelineFragment = (TimelineFragment) fm.getFragment(savedInstanceState, TimelineFragment.class.getSimpleName());
            mGirlsFragment = (GirlsFragment) fm.getFragment(savedInstanceState, GirlsFragment.class.getSimpleName());
            mFavoriteFragment = (FavoriteFragment) fm.getFragment(savedInstanceState, FavoriteFragment.class.getSimpleName());
            mMyInfoFragment = (MyInfoFragment) fm.getFragment(savedInstanceState, MyInfoFragment.class.getSimpleName());
        }

        if (!mTimelineFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.main_container, mTimelineFragment, TimelineFragment.class.getSimpleName())
                    .commit();
        }

        if (!mGirlsFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.main_container, mGirlsFragment, GirlsFragment.class.getSimpleName())
                    .commit();
        }

        if (!mFavoriteFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.main_container, mFavoriteFragment, FavoriteFragment.class.getSimpleName())
                    .commit();
        }

        if (!mMyInfoFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.main_container, mMyInfoFragment, MyInfoFragment.class.getSimpleName())
                    .commit();
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        if (fragment instanceof TimelineFragment) {
            fm.beginTransaction()
                    .show(mTimelineFragment)
                    .hide(mGirlsFragment)
                    .hide(mFavoriteFragment)
                    .hide(mMyInfoFragment)
                    .commit();
        } else if (fragment instanceof GirlsFragment) {
            fm.beginTransaction()
                    .hide(mTimelineFragment)
                    .show(mGirlsFragment)
                    .hide(mFavoriteFragment)
                    .hide(mMyInfoFragment)
                    .commit();
        } else if (fragment instanceof FavoriteFragment) {
            fm.beginTransaction()
                    .hide(mTimelineFragment)
                    .hide(mGirlsFragment)
                    .show(mFavoriteFragment)
                    .hide(mMyInfoFragment)
                    .commit();
        } else if (fragment instanceof MyInfoFragment) {
            fm.beginTransaction()
                    .hide(mTimelineFragment)
                    .hide(mGirlsFragment)
                    .hide(mFavoriteFragment)
                    .show(mMyInfoFragment)
                    .commit();
        }
    }
}
