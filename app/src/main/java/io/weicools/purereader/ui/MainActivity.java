package io.weicools.purereader.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import io.weicools.purereader.R;
import io.weicools.purereader.base.BaseFragment;
import io.weicools.purereader.ui.favorite.FavoriteFragment;
import io.weicools.purereader.ui.meizi.GirlsFragment;
import io.weicools.purereader.ui.setting.MyInfoFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2017/12/2
 *
 * desc: Main UI for displaying the {@link ViewPager}
 */
public class MainActivity extends AppCompatActivity {
  private static final int BOTTOM_COUNT = 4;

  @BindView(R.id.view_pager) AHBottomNavigationViewPager mViewPager;
  @BindView(R.id.bottom_navigation) AHBottomNavigation mBottomNavigation;

  private BaseFragment mCurrFragment;
  private MainViewPagerAdapter mViewPagerAdapter;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    initViews();
    // FIXME: 2017/12/2 new FavoritePresenter
  }

  private void initViews () {
    MainFragment mainFragment = MainFragment.newInstance();
    GirlsFragment girlsFragment = GirlsFragment.newInstance();
    FavoriteFragment favoriteFragment = FavoriteFragment.newInstance();
    MyInfoFragment myInfoFragment = MyInfoFragment.newInstance();
    List<Fragment> fragmentList = new ArrayList<>(BOTTOM_COUNT);
    fragmentList.add(mainFragment);
    fragmentList.add(girlsFragment);
    fragmentList.add(favoriteFragment);
    fragmentList.add(myInfoFragment);
    mViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
    mViewPager.setOffscreenPageLimit(3);
    mViewPager.setAdapter(mViewPagerAdapter);

    AHBottomNavigationItem mainItem =
        new AHBottomNavigationItem(R.string.nav_timeline, R.drawable.ic_bottom_library_books, R.color.color_tab_1);
    AHBottomNavigationItem girlItem =
        new AHBottomNavigationItem(R.string.nav_girls, R.drawable.ic_bottom_photo, R.color.color_tab_2);
    AHBottomNavigationItem favoriteItem =
        new AHBottomNavigationItem(R.string.nav_favorite, R.drawable.ic_bottom_favorite, R.color.color_tab_3);
    AHBottomNavigationItem myInfoItem =
        new AHBottomNavigationItem(R.string.nav_info, R.drawable.ic_bottom_info_outline, R.color.color_tab_4);
    List<AHBottomNavigationItem> navigationItemList = new ArrayList<>(BOTTOM_COUNT);
    navigationItemList.add(mainItem);
    navigationItemList.add(girlItem);
    navigationItemList.add(favoriteItem);
    navigationItemList.add(myInfoItem);
    mBottomNavigation.addItems(navigationItemList);
    mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    mBottomNavigation.setTranslucentNavigationEnabled(true);
    mBottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
      if (mCurrFragment == null) {
        mCurrFragment = (BaseFragment) mViewPagerAdapter.getItem(position);
      }
      if (wasSelected) {
        mCurrFragment.refresh();
        return true;
      }

      if (mCurrFragment != null) {
        mCurrFragment.willBeHidden();
      }
      mViewPager.setCurrentItem(position, false);
      if (mCurrFragment == null) {
        return true;
      }

      mCurrFragment = (BaseFragment) mViewPagerAdapter.getItem(position);
      mCurrFragment.willBeDisplayed();
      return true;
    });
  }
}
