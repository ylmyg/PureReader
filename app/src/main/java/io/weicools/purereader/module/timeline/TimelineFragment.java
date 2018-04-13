package io.weicools.purereader.module.timeline;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.weicools.purereader.R;
import io.weicools.purereader.data.local.ZhihuDailyNewsLocalDataSource;
import io.weicools.purereader.data.remote.ZhihuDailyNewsRemoteDataSource;
import io.weicools.purereader.data.repository.ZhihuDailyNewsRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {
    private FloatingActionButton mFab;
    private TabLayout mTabLayout;

    private ZhihuDailyFragment mZhihuFragment;

    public TimelineFragment() {
        // Required empty public constructor
    }

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentManager fm = getChildFragmentManager();
            mZhihuFragment = (ZhihuDailyFragment) fm.getFragment(savedInstanceState, ZhihuDailyFragment.class.getSimpleName());
        } else {
            mZhihuFragment = ZhihuDailyFragment.newInstance();
        }

        // FIXME: 2017/12/3 Presenter Zhihu, Gank, Douban
        new ZhihuDailyPresenter(mZhihuFragment, ZhihuDailyNewsRepository.getInstance(
                ZhihuDailyNewsLocalDataSource.getInstance(getContext()),
                ZhihuDailyNewsRemoteDataSource.getInstance()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        initView(view);
        initListener();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fm = getChildFragmentManager();
        if (mZhihuFragment.isAdded()) {
            fm.putFragment(outState, ZhihuDailyFragment.class.getSimpleName(), mZhihuFragment);
        }
    }

    private void initView(View view) {
        // FIXME: 2017/12/3 adapter Gank,Douban
        ViewPager mViewPager = view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TimelineFragmentPagerAdapter(
                getChildFragmentManager(),
                getContext(),
                mZhihuFragment));
        mViewPager.setOffscreenPageLimit(3);

        mTabLayout = view.findViewById(R.id.timeline_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mFab = view.findViewById(R.id.fab);
    }

    private void initListener() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    mZhihuFragment.showDatePickerDialog();
                } else {
                    //mDoubanFragment.showDatePickerDialog();
                }
            }
        });
    }
}
