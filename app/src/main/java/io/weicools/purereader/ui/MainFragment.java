package io.weicools.purereader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import io.weicools.purereader.AppConfig;
import io.weicools.purereader.R;
import io.weicools.purereader.base.BaseFragment;
import io.weicools.purereader.ui.gank.DailyGankFragment;
import io.weicools.purereader.ui.gank.GankFragment;
import io.weicools.purereader.ui.search.SearchActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2017/12/2
 *
 * desc:
 */
public class MainFragment extends BaseFragment {
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.timeline_tab_layout) TabLayout mTabLayout;
  @BindView(R.id.view_pager) ViewPager mViewPager;
  @BindView(R.id.fab) FloatingActionButton mFab;

  private DailyGankFragment mGankFragment;
  private GankFragment mAndroidFragment;
  private GankFragment miOSFragment;
  private GankFragment mWebFontFragment;
  private GankFragment mAppFragment;
  private GankFragment mRecommendFragment;

  public static MainFragment newInstance () {
    return new MainFragment();
  }

  @Override protected int getLayoutResId () {
    return R.layout.fragment_main;
  }

  @Override public void onCreate (@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    // FIXME: 2018/4/16 night mode problem
    mGankFragment = DailyGankFragment.newInstance();
    mAndroidFragment = GankFragment.newInstance(AppConfig.TYPE_ANDROID);
    miOSFragment = GankFragment.newInstance(AppConfig.TYPE_IOS);
    mWebFontFragment = GankFragment.newInstance(AppConfig.TYPE_WEB_FONT);
    mAppFragment = GankFragment.newInstance(AppConfig.TYPE_APP);
    mRecommendFragment = GankFragment.newInstance(AppConfig.TYPE_RECOMMEND);
  }

  @Override
  public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    initToolbar();
    initView();
    initListener();
    return mRootView;
  }

  public void initToolbar () {
    AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
    if (compatActivity != null) {
      compatActivity.setSupportActionBar(mToolbar);
      ActionBar actionBar = compatActivity.getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(false);
      }
    }
  }

  @Override public void onSaveInstanceState (@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    FragmentManager fm = getChildFragmentManager();
    if (mGankFragment.isAdded()) {
      fm.putFragment(outState, AppConfig.TYPE_DAILY, mGankFragment);
    }
    if (mAndroidFragment.isAdded()) {
      fm.putFragment(outState, AppConfig.TYPE_ANDROID, mAndroidFragment);
    }
    if (miOSFragment.isAdded()) {
      fm.putFragment(outState, AppConfig.TYPE_IOS, miOSFragment);
    }
    if (mWebFontFragment.isAdded()) {
      fm.putFragment(outState, AppConfig.TYPE_WEB_FONT, mWebFontFragment);
    }
    if (mAppFragment.isAdded()) {
      fm.putFragment(outState, AppConfig.TYPE_APP, mAppFragment);
    }
    if (mRecommendFragment.isAdded()) {
      fm.putFragment(outState, AppConfig.TYPE_RECOMMEND, mRecommendFragment);
    }
  }

  private void initView () {
    List<String> titles = new ArrayList<>();
    titles.add(AppConfig.TYPE_DAILY);
    titles.add(AppConfig.TYPE_ANDROID);
    titles.add(AppConfig.TYPE_IOS);
    titles.add(AppConfig.TYPE_WEB_FONT);
    titles.add(AppConfig.TYPE_APP);
    titles.add(AppConfig.TYPE_RECOMMEND);
    mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
    mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
    mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
    mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(3)));
    mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(4)));
    mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(5)));

    List<Fragment> fragments = new ArrayList<>();
    fragments.add(mGankFragment);
    fragments.add(mAndroidFragment);
    fragments.add(miOSFragment);
    fragments.add(mWebFontFragment);
    fragments.add(mAppFragment);
    fragments.add(mRecommendFragment);

    FragmentAdapter mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fragments, titles);
    mViewPager.setOffscreenPageLimit(5);
    mViewPager.setAdapter(mFragmentAdapter);
    mTabLayout.setupWithViewPager(mViewPager);
  }

  private void initListener () {
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) { }

      @Override public void onPageSelected (int position) {
        if (position == 0) {
          mFab.show();
        } else {
          mFab.hide();
        }
      }

      @Override public void onPageScrollStateChanged (int state) { }
    });

    mFab.setOnClickListener(view -> {
      if (mTabLayout.getSelectedTabPosition() == 0) {
        mGankFragment.showDatePickerDialog();
      }
    });
  }

  @Override public boolean onOptionsItemSelected (MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_search:
        startActivity(new Intent(getActivity(), SearchActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_gank, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  static class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mTitles;

    FragmentAdapter (FragmentManager fm, List<Fragment> fragments, List<String> titles) {
      super(fm);
      mFragments = fragments;
      mTitles = titles;
    }

    @Override public Fragment getItem (int position) {
      return mFragments.get(position);
    }

    @Override public int getCount () {
      return mFragments.size();
    }

    @Override public CharSequence getPageTitle (int position) {
      return mTitles.get(position);
    }
  }
}
