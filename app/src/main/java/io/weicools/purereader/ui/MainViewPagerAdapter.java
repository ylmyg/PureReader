package io.weicools.purereader.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * @author Weicools create on 2018.08.12
 *
 * desc:
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
  private final List<Fragment> mFragmentList;

  public MainViewPagerAdapter (FragmentManager fm, @NonNull List<Fragment> fragments) {
    super(fm);
    mFragmentList = fragments;
  }

  @Override
  public Fragment getItem (int position) {
    return mFragmentList.get(position);
  }

  @Override
  public int getCount () {
    return mFragmentList.size();
  }
}
