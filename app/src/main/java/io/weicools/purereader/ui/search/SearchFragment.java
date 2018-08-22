package io.weicools.purereader.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.nex3z.flowlayout.FlowLayout;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.R;
import io.weicools.purereader.api.GankRetrofit;
import io.weicools.purereader.data.SearchResult;
import io.weicools.purereader.database.ReaderDatabase;
import io.weicools.purereader.ui.LoadMoreRecyclerOnScrollListener;
import io.weicools.purereader.util.DateTimeUtil;
import io.weicools.purereader.util.ToastUtil;
import java.util.List;

/**
 * @author Weicools Create on 2018.08.15
 *
 * desc:
 */
public class SearchFragment extends Fragment {
  public static final String ARG_CATEGORY = "category";
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
  @BindView(R.id.flow_layout) FlowLayout mFlowLayout;
  @BindView(R.id.cv_search_history) CardView mCvSearchHistory;
  @BindView(R.id.progress_bar) ProgressBar mProgressBar;
  @BindView(R.id.tv_no_search_history) TextView mTvNoSearchHistory;
  Unbinder unbind;

  private String mCategory, mKeyword;
  private SearchViewAdapter mViewAdapter;
  private CompositeDisposable mDisposable;

  public static SearchFragment newInstance (String category) {
    SearchFragment fragment = new SearchFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ARG_CATEGORY, category);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate (@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mCategory = getArguments().getString(ARG_CATEGORY);
    }
  }

  @Nullable
  @Override
  public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frgment_search, container, false);
    unbind = ButterKnife.bind(this, view);
    mDisposable = new CompositeDisposable();
    return view;
  }

  @Override
  public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewAdapter = new SearchViewAdapter(view.getContext());
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mViewAdapter);
    mRecyclerView.addOnScrollListener(new LoadMoreRecyclerOnScrollListener(layoutManager) {
      @Override
      public void onLoadMore (int currentPage) {
        loadSearchData(mKeyword, currentPage, true);
      }
    });
    loadSearchHistory();
  }

  @OnClick(R.id.tv_clear_history)
  void onClickClearHistory () {
    mDisposable.add(Completable.fromAction(() -> ReaderDatabase.getInstance().historyDao().deleteAllHistory())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> ToastUtil.showShort("Clear finish"), throwable -> ToastUtil.showShort("occur error...")));
  }

  @Override
  public void onDestroyView () {
    super.onDestroyView();
    unbind.unbind();
  }

  public void loadSearchHistory () {
    mRecyclerView.setVisibility(View.INVISIBLE);
    mCvSearchHistory.setVisibility(View.VISIBLE);
    mDisposable.add(ReaderDatabase.getInstance()
        .historyDao()
        .getHistoryKeyword()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(strings -> {
          if (strings == null || strings.size() == 0) {
            mTvNoSearchHistory.setVisibility(View.VISIBLE);
          } else {
            mTvNoSearchHistory.setVisibility(View.INVISIBLE);
            mFlowLayout.removeAllViews();
            for (String s : strings) {
              mFlowLayout.addView(buildHistoryText(s));
            }
          }
        }, throwable -> {
          mTvNoSearchHistory.setVisibility(View.VISIBLE);
          ToastUtil.showShort("get history error, " + throwable.getMessage());
        }));
  }

  public void loadSearchData (String keyword, int page, boolean isLoadMore) {
    if (page == 1) {
      mKeyword = keyword;
    }
    if (isAdded()) {
      mProgressBar.setVisibility(View.VISIBLE);
      mCvSearchHistory.setVisibility(View.INVISIBLE);
      mDisposable.add(GankRetrofit.getInstance().getGankApi().getSearchData(keyword, mCategory, 10, page).map(data -> {
        List<SearchResult> resultList = data.getSearchResults();
        if (resultList == null) {
          return null;
        }
        for (SearchResult result : resultList) {
          result.setPublishedAt(DateTimeUtil.dateFormat(result.getPublishedAt(), DateTimeUtil.DATE_FORMAT_STYLE6,
              DateTimeUtil.DATE_FORMAT_STYLE4));
        }
        return resultList;
      }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultList -> {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (resultList != null) {
          mRecyclerView.setVisibility(View.VISIBLE);
          if (isLoadMore) {
            mViewAdapter.updateSearchResult(resultList);
          } else {
            mViewAdapter.setSearchResult(resultList);
          }
        }
      }, throwable -> {
        mProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "search error", Toast.LENGTH_SHORT).show();
      }));
    }
  }

  private TextView buildHistoryText (String text) {
    TextView textView = new TextView(getContext());
    textView.setText(text);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    textView.setPadding((int) dpToPx(16), (int) dpToPx(8), (int) dpToPx(16), (int) dpToPx(8));
    textView.setBackgroundResource(R.drawable.bg_search_history_text);
    textView.setOnClickListener(v -> {
      SearchActivity activity = (SearchActivity) getActivity();
      if (activity != null) {
        activity.updateSearchKeyword(text);
        activity.loadSearchResult(text);
      }
    });

    return textView;
  }

  private float dpToPx (float dp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
  }
}
