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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.weicools.purereader.AppConfig;
import io.weicools.purereader.R;
import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.ui.DatePickerDialog;
import java.util.Calendar;
import java.util.List;

/**
 * @author Weicools Create on 2018.04.12
 * Use the {@link DailyGankFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * show daily gank
 */
public class DailyGankFragment extends Fragment implements GankContract.View {
  @BindView(R.id.rv_article) RecyclerView mRecyclerView;
  @BindView(R.id.empty_view) LinearLayout mEmptyView;
  @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
  Unbinder unbinder;

  private GankAdapter mAdapter;
  private GankContract.Presenter mPresenter;

  private int mYear, mMonth, mDay;
  private boolean mIsFirstLoad = true;

  public static DailyGankFragment newInstance () {
    return new DailyGankFragment();
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_gank, container, false);
    unbinder = ButterKnife.bind(this, view);

    new GankPresenter(this);
    mAdapter = new GankAdapter(getContext(), GankAdapter.LIST_TYPE_DAILY);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);

    mRefreshLayout.setOnRefreshListener(() -> mPresenter.loadLatestDailyData());

    return view;
  }

  @Override
  public void onResume () {
    super.onResume();
    setLoadingIndicator(mIsFirstLoad);
    if (mIsFirstLoad) {
      mPresenter.loadLatestDailyData();
      mIsFirstLoad = false;
    }
  }

  @Override
  public void onDestroyView () {
    super.onDestroyView();
    unbinder.unbind();
    mPresenter.unSubscribe();
  }

  public void showDatePickerDialog () {
    final Calendar c = Calendar.getInstance();
    if (mYear != 0 && mMonth != 0 && mDay != 0) {
      c.set(mYear, mMonth, mDay);
    }

    Calendar minDate = Calendar.getInstance();
    minDate.set(2015, 5, 18);
    DatePickerDialog dialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
      mYear = year;
      mMonth = monthOfYear;
      mDay = dayOfMonth;
      mPresenter.loadDailyData(mYear, mMonth + 1, mDay);
    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), Calendar.getInstance(), minDate);

    dialog.show(getChildFragmentManager(), AppConfig.TYPE_DAILY);
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
    mRefreshLayout.setRefreshing(false);
    mEmptyView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showNoData () {
    mEmptyView.setVisibility(View.VISIBLE);
  }
}
