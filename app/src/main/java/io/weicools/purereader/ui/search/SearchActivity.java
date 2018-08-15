package io.weicools.purereader.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.weicools.purereader.AppConfig;
import io.weicools.purereader.R;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018.08.15
 *
 * desc:
 */
public class SearchActivity extends AppCompatActivity {

  @BindView(R.id.iv_back) ImageView mIvBack;
  @BindView(R.id.iv_clear_keyword) ImageView mIvClearKeyword;
  @BindView(R.id.edit_query) TextInputEditText mEditQuery;
  @BindView(R.id.search_tab_layout) TabLayout mTabLayout;
  @BindView(R.id.view_pager) ViewPager mViewPager;

  private SearchFragment mAllFragment;
  private SearchFragment mAndroidFragment;
  private SearchFragment mIOSFragment;
  private SearchFragment mWebFontFragment;
  private SearchFragment mAppFragment;
  private SearchPagerAdapter mPagerAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);

    initViews();
  }


  private void initViews() {
    String[] titles = { getString(R.string.category_all), getString(R.string.category_android),
        getString(R.string.category_ios), getString(R.string.category_web_font),
        getString(R.string.category_app) };
    mAllFragment = SearchFragment.newInstance(AppConfig.TYPE_ALL);
    mAndroidFragment = SearchFragment.newInstance(AppConfig.TYPE_ANDROID);
    mIOSFragment = SearchFragment.newInstance(AppConfig.TYPE_IOS);
    mWebFontFragment = SearchFragment.newInstance(AppConfig.TYPE_WEB_FONT);
    mAppFragment = SearchFragment.newInstance(AppConfig.TYPE_APP);
    List<Fragment> fragments = new ArrayList<>(titles.length);
    fragments.add(mAllFragment);
    fragments.add(mAndroidFragment);
    fragments.add(mIOSFragment);
    fragments.add(mWebFontFragment);
    fragments.add(mAppFragment);

    mPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), fragments, titles);
    mViewPager.setOffscreenPageLimit(4);
    mViewPager.setAdapter(mPagerAdapter);
    mTabLayout.setupWithViewPager(mViewPager);

    mEditQuery.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }


      @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }


      @Override public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(mEditQuery.getText().toString().trim())) {
          mPagerAdapter.loadSearchResult(mEditQuery.getText().toString(), 1);
        }
      }
    });
  }


  @OnClick(R.id.iv_back) public void onBackClicked() {
    finish();
  }


  @OnClick(R.id.iv_back) public void onClearSearchKeyword() {
    mEditQuery.setText("");
  }


  static class SearchPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList;
    private final String[] mTitles;


    SearchPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
      super(fm);
      mFragmentList = fragmentList;
      mTitles = titles;
    }


    @Override public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }


    @Override public int getCount() {
      return mTitles.length;
    }


    @Nullable @Override public CharSequence getPageTitle(int position) {
      return mTitles[position];
    }


    public void loadSearchResult(String keyword, int page) {
      for (Fragment fragment : mFragmentList) {
        ((SearchFragment) fragment).loadSearchData(keyword, page);
      }
    }
  }
}
