package io.weicools.purereader.ui.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.weicools.purereader.data.GankContent;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.weicools.purereader.R;
import io.weicools.purereader.ui.LoadMoreRecyclerOnScrollListener;

/**
 * @author Weicools Create on 2018.04.12
 * Use the {@link GankFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * show category gank
 */
public class GankFragment extends Fragment implements GankContract.View {
  private static final String ARG_CATEGORY = "arg_category";

  @BindView(R.id.rv_article) RecyclerView mRecyclerView;
  @BindView(R.id.empty_view) LinearLayout mEmptyView;
  @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
  Unbinder unbinder;

  private String category;
  private GankAdapter mAdapter;
  private GankContract.Presenter mPresenter;

  private int currPage = 1;
  private boolean mIsFirstLoad = true;

  public static GankFragment newInstance (String categoryKey) {
    GankFragment fragment = new GankFragment();
    Bundle args = new Bundle();
    args.putString(ARG_CATEGORY, categoryKey);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle bundle = getArguments();
    if (bundle != null) {
      category = getArguments().getString(ARG_CATEGORY);
    }
  }

  @Override
  public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_gank, container, false);
    unbinder = ButterKnife.bind(this, view);

    new GankPresenter(this);
    mAdapter = new GankAdapter(getContext());
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);

    mRefreshLayout.setOnRefreshListener(() -> mPresenter.loadGankData(true, category, 1));
    mRecyclerView.addOnScrollListener(new LoadMoreRecyclerOnScrollListener(layoutManager) {
      @Override
      public void onLoadMore (int currentPage) {
        currPage = currentPage;
        mPresenter.loadGankData(false, category, currPage);
      }
    });

    return view;
  }

  @Override
  public void onResume () {
    super.onResume();
    setLoadingIndicator(mIsFirstLoad);
    if (mIsFirstLoad) {
      mPresenter.loadGankData(false, category, 1);
      mIsFirstLoad = false;
    }
  }

  @Override
  public void onDestroyView () {
    super.onDestroyView();
    unbinder.unbind();
    mPresenter.unSubscribe();
  }

  @Override
  public void setPresenter (GankContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public void setLoadingIndicator (boolean active) {
    mRefreshLayout.setRefreshing(active);
  }

  @Override
  public void showResult (List<GankContent> dataList) {
    mEmptyView.setVisibility(View.GONE);
    mAdapter.setDataList(dataList);
  }

  @Override
  public void updateResult (List<GankContent> dataList) {
    mEmptyView.setVisibility(View.GONE);
    mAdapter.updateData(dataList);
  }

  @Override
  public void showLoadingDataError () {
    mEmptyView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showNoData () {
    mEmptyView.setVisibility(View.VISIBLE);
  }
}
