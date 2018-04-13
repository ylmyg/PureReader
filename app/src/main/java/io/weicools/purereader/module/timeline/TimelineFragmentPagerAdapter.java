package io.weicools.purereader.module.timeline;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.weicools.purereader.R;

/**
 * Create by weicools on 2017/12/3.
 * <p>
 * {@link FragmentPagerAdapter} of {@link TimelineFragment}.
 */

public class TimelineFragmentPagerAdapter extends FragmentPagerAdapter {

    // TODO: 2017/12/3 Gank, Douban
    private final int pageCount = 1;
    private String[] titles;

    private ZhihuDailyFragment mZhihuFragment;

    TimelineFragmentPagerAdapter(FragmentManager fm, Context context,
                                 ZhihuDailyFragment zhihuDailyFragment) {
        super(fm);
        titles = new String[]{context.getString(R.string.zhihu_daily)};
        this.mZhihuFragment = zhihuDailyFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mZhihuFragment;
        }
        return mZhihuFragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
