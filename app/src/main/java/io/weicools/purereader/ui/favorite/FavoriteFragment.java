package io.weicools.purereader.ui.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import io.weicools.purereader.R;
import io.weicools.purereader.base.BaseFragment;
import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.ui.gank.GankAdapter;
import java.util.List;

/**
 * @author Weicools create on 2018.04.14
 *
 * desc:
 */
public class FavoriteFragment extends BaseFragment implements FavoritesContract.View {
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
  @BindView(R.id.empty_view) LinearLayout mEmptyView;
  @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;

  private GankAdapter mAdapter;
  private FavoritesContract.Presenter mPresenter;

  public static FavoriteFragment newInstance () {
    return new FavoriteFragment();
  }

  @Override
  protected int getLayoutResId () {
    return R.layout.fragment_favorite;
  }

  @Override
  public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    new FavoritesPresenter(this);
    mAdapter = new GankAdapter(getContext(), GankAdapter.LIST_TYPE_FAVORITE);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mRootView.getContext(), R.color.colorAccent));
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRefreshLayout.setOnRefreshListener(() -> mPresenter.loadFavorites());
    return mRootView;
  }

  @Override
  public void onResume () {
    super.onResume();
    mPresenter.loadFavorites();
  }

  @Override
  public void setPresenter (FavoritesContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public void setLoadingIndicator (boolean active) {
    mRefreshLayout.setRefreshing(active);
  }

  @Override
  public void showFavorites (List<GankContent> dataList) {
    setLoadingIndicator(false);
    mAdapter.setDataList(dataList);
  }

  @Override
  public void showNoData () {
    mEmptyView.setVisibility(View.VISIBLE);
  }
}
