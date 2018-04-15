package io.weicools.purereader.module.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.weicools.purereader.AppConfig;
import io.weicools.purereader.R;
import io.weicools.purereader.module.gank.GankFragment;
import io.weicools.purereader.module.search.MaterialSearchView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {
    @BindView(R.id.timeline_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    Unbinder unbinder;
//    @BindView(R.id.search_view)
//    MaterialSearchView mSearchView;

    //private ZhihuDailyFragment mZhihuFragment;
    private GankFragment mGankFragment;
    private GankFragment mAndroidFragment;
    private GankFragment miOSFragment;
    private GankFragment mWebFontFragment;
    private GankFragment mRecommendFragment;

    public TimelineFragment() {
        // Required empty public constructor
    }

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            FragmentManager fm = getChildFragmentManager();
            //mZhihuFragment = (ZhihuDailyFragment) fm.getFragment(savedInstanceState, ZhihuDailyFragment.class.getSimpleName());
            mGankFragment = (GankFragment) fm.getFragment(savedInstanceState, AppConfig.TYPE_ALL);
            mAndroidFragment = (GankFragment) fm.getFragment(savedInstanceState, AppConfig.TYPE_ANDROID);
            miOSFragment = (GankFragment) fm.getFragment(savedInstanceState, AppConfig.TYPE_IOS);
            mWebFontFragment = (GankFragment) fm.getFragment(savedInstanceState, AppConfig.TYPE_WEB_FONT);
            mRecommendFragment = (GankFragment) fm.getFragment(savedInstanceState, AppConfig.TYPE_RECOMMEND);
        } else {
            //mZhihuFragment = ZhihuDailyFragment.newInstance();
            mGankFragment = GankFragment.newInstance(AppConfig.TYPE_ALL);
            mAndroidFragment = GankFragment.newInstance(AppConfig.TYPE_ANDROID);
            miOSFragment = GankFragment.newInstance(AppConfig.TYPE_IOS);
            mWebFontFragment = GankFragment.newInstance(AppConfig.TYPE_WEB_FONT);
            mRecommendFragment = GankFragment.newInstance(AppConfig.TYPE_RECOMMEND);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        initListener();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fm = getChildFragmentManager();
//        if (mZhihuFragment.isAdded()) {
//            fm.putFragment(outState, ZhihuDailyFragment.class.getSimpleName(), mZhihuFragment);
//        }

        if (mGankFragment.isAdded()) {
            fm.putFragment(outState, AppConfig.TYPE_ALL, mGankFragment);
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

        if (mRecommendFragment.isAdded()) {
            fm.putFragment(outState, AppConfig.TYPE_RECOMMEND, mRecommendFragment);
        }
    }

    private void initView() {
        List<String> titles = new ArrayList<>();
        //titles.add(getString(R.string.tab_title_main_1));
        titles.add(getString(R.string.tab_title_main_2));
        titles.add(getString(R.string.tab_title_main_3));
        titles.add(AppConfig.TYPE_IOS);
        titles.add(AppConfig.TYPE_WEB_FONT);
        titles.add(AppConfig.TYPE_RECOMMEND);
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(3)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(4)));
        //mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(5)));

        List<Fragment> fragments = new ArrayList<>();
        //fragments.add(mZhihuFragment);
        fragments.add(mGankFragment);
        fragments.add(mAndroidFragment);
        fragments.add(miOSFragment);
        fragments.add(mWebFontFragment);
        fragments.add(mRecommendFragment);

        mViewPager.setOffscreenPageLimit(5);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mFab.show();
                } else {
                    mFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    //mZhihuFragment.showDatePickerDialog();
                } else {
                    //mDoubanFragment.showDatePickerDialog();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_search) {
//
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_gank, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        mSearchView.setMenuItem(item);
    }

    static class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mTitles;

        FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
