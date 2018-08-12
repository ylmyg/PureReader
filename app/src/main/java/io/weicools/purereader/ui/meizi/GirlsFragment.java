package io.weicools.purereader.ui.meizi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.weicools.purereader.base.BaseFragment;
import io.weicools.purereader.data.GankContent;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.weicools.purereader.AppConfig;
import io.weicools.purereader.R;
import io.weicools.purereader.ui.LoadMoreRecyclerOnScrollListener;
import io.weicools.purereader.ui.gank.GankContract;
import io.weicools.purereader.ui.gank.GankPresenter;

/**
 * @author Weicools Create on 2018.04.13
 *
 * desc: show girls
 */
public class GirlsFragment extends BaseFragment implements GankContract.View {
  @BindView(R.id.rv_girls) RecyclerView mRecyclerView;
  @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;

  private GirlAdapter mAdapter;
  private GankContract.Presenter mPresenter;
  private int currPage = 1;
  private boolean mIsFirstLoad = true;


  public static GirlsFragment newInstance() {
    return new GirlsFragment();
  }


  @Override protected int getLayoutResId() {
    return R.layout.fragment_girls;
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    new GankPresenter(this);
    mAdapter = new GirlAdapter(getContext());
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mRootView.getContext(), R.color.colorAccent));
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);

    mRefreshLayout.setOnRefreshListener(() -> mPresenter.loadGankData(true, AppConfig.TYPE_GIRLS, 1));

    mRecyclerView.addOnScrollListener(new LoadMoreRecyclerOnScrollListener(layoutManager) {
      @Override
      public void onLoadMore(int currentPage) {
        currPage = currentPage;
        mPresenter.loadGankData(false, AppConfig.TYPE_GIRLS, currPage);
      }
    });

    return mRootView;
  }


  @Override
  public void onResume() {
    super.onResume();
    setLoadingIndicator(mIsFirstLoad);
    if (mIsFirstLoad) {
      mPresenter.loadGankData(false, AppConfig.TYPE_GIRLS, 1);
      mIsFirstLoad = false;
    } else {
      //mPresenter.loadGankData(category, 10, currPage);
    }
  }


  @Override
  public void setPresenter(GankContract.Presenter presenter) {
    mPresenter = presenter;
  }


  @Override
  public void setLoadingIndicator(boolean active) {
    mRefreshLayout.setRefreshing(active);
  }


  @Override
  public void showResult(List<GankContent> dataList) {
    mAdapter.setDataList(dataList);
  }


  @Override
  public void updateResult(List<GankContent> dataList) {
    mAdapter.updateData(dataList);
  }


  @Override
  public void showLoadingDataError() {
    //mEmptyView.setVisibility(View.VISIBLE);
  }


  @Override
  public void showNoData() {
    //mEmptyView.setVisibility(View.VISIBLE);
  }
}
