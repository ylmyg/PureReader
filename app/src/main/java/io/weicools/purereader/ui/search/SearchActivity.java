package io.weicools.purereader.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.AppConfig;
import io.weicools.purereader.R;
import io.weicools.purereader.data.SearchHistory;
import io.weicools.purereader.database.ReaderDatabase;
import io.weicools.purereader.util.DateTimeUtil;
import io.weicools.purereader.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018.08.15
 *
 * desc:
 */
public class SearchActivity extends AppCompatActivity {

  //@BindView(R.id.iv_back) ImageView mIvBack;
  @BindView(R.id.iv_clear_keyword) ImageView mIvClearKeyword;
  @BindView(R.id.edit_query) EditText mEditQuery;
  @BindView(R.id.search_tab_layout) TabLayout mTabLayout;
  @BindView(R.id.view_pager) ViewPager mViewPager;

  private SearchFragment mAllFragment;
  private SearchFragment mAndroidFragment;
  private SearchFragment mIOSFragment;
  private SearchFragment mWebFontFragment;
  private SearchFragment mAppFragment;
  private SearchPagerAdapter mPagerAdapter;
  private CompositeDisposable mDisposable;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);
    mDisposable = new CompositeDisposable();
    initViews();
  }

  @SuppressLint("ClickableViewAccessibility")
  private void initViews () {
    String[] titles = {
        getString(R.string.category_all), getString(R.string.category_android), getString(R.string.category_ios),
        getString(R.string.category_web_font), getString(R.string.category_app)
    };
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
    mEditQuery.setOnTouchListener((view, motionEvent) -> {
      mPagerAdapter.loadSearchHistory();
      return false;
    });
    mEditQuery.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged (CharSequence s, int start, int count, int after) { }

      @Override
      public void onTextChanged (CharSequence s, int start, int before, int count) { }

      @Override
      public void afterTextChanged (Editable s) {
        if (!TextUtils.isEmpty(mEditQuery.getText().toString())) {
          mIvClearKeyword.setVisibility(View.VISIBLE);
        } else {
          mIvClearKeyword.setVisibility(View.INVISIBLE);
        }
      }
    });
    mEditQuery.setOnEditorActionListener((textView, actionId, event) -> {
      //当actionId == XX_SEND 或者 XX_DONE时都触发
      //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
      //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
      if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || (event != null
          && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
          && KeyEvent.ACTION_DOWN == event.getAction())) {
        onClickSearch();
      }
      return false;
    });
  }

  public void updateSearchKeyword (String keyword) {
    mEditQuery.setText(keyword);
  }

  @OnClick(R.id.iv_back)
  public void onClickBack () {
    finish();
  }

  @OnClick(R.id.iv_clear_keyword)
  public void onClickClear () {
    mEditQuery.setText("");
  }

  @OnClick(R.id.iv_search)
  public void onClickSearch () {
    String keyword = mEditQuery.getText().toString();
    if (!TextUtils.isEmpty(keyword.trim())) {
      mDisposable.add(Completable.fromAction(() -> ReaderDatabase.getInstance()
          .historyDao()
          .insertSearchHistory(new SearchHistory(keyword, DateTimeUtil.getCurrTime())))
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(() -> {
          }, throwable -> Log.e("zzw", "save keyword error: ", throwable)));

      mPagerAdapter.loadSearchResult(mEditQuery.getText().toString());
    } else {
      ToastUtil.showShort("Please input search keyword!");
    }
  }

  public void loadSearchResult(String keyword) {
    mPagerAdapter.loadSearchResult(mEditQuery.getText().toString());
  }

  @Override
  protected void onDestroy () {
    super.onDestroy();
    mDisposable.dispose();
  }

  static class SearchPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList;
    private final String[] mTitles;

    SearchPagerAdapter (FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
      super(fm);
      mFragmentList = fragmentList;
      mTitles = titles;
    }

    @Override
    public Fragment getItem (int position) {
      return mFragmentList.get(position);
    }

    @Override
    public int getCount () {
      return mTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle (int position) {
      return mTitles[position];
    }

    public void loadSearchResult (String keyword) {
      for (Fragment fragment : mFragmentList) {
        ((SearchFragment) fragment).loadSearchData(keyword, 1, false);
      }
    }

    public void loadSearchHistory () {
      for (Fragment fragment : mFragmentList) {
        ((SearchFragment) fragment).loadSearchHistory();
      }
    }
  }
}
