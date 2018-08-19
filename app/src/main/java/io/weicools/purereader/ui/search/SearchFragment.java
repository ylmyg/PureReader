package io.weicools.purereader.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.R;
import io.weicools.purereader.api.GankRetrofit;
import io.weicools.purereader.data.SearchResult;
import io.weicools.purereader.database.ReaderDatabase;
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
  Unbinder unbind;

  private String mCategory;
  private SearchViewAdapter mViewAdapter;
  private SearchHistoryAdapter mHistoryAdapter;
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
    mHistoryAdapter = new SearchHistoryAdapter(view.getContext(), new SearchHistoryAdapter.OnClickSearchListener() {
      @Override
      public void onClickHistoryItem (String s) {
        SearchActivity activity = (SearchActivity) getActivity();
        if (activity != null) {
          activity.updateSearchKeyword(s);
        }
      }

      @Override
      public void onClickClearHistory () {
        mDisposable.add(Completable.fromAction(() -> ReaderDatabase.getInstance().historyDao().deleteAllHistory())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> ToastUtil.showShort("Clear finish"), throwable -> ToastUtil.showShort("occur error...")));
      }
    });
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mHistoryAdapter);
    loadSearchHistory();
  }

  @Override
  public void onDestroyView () {
    super.onDestroyView();
    unbind.unbind();
  }

  public void loadSearchHistory () {
    mDisposable.add(ReaderDatabase.getInstance()
        .historyDao()
        .getHistoryKeyword()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(strings -> mHistoryAdapter.updateSearchHistory(strings), throwable -> {
          Log.e("zzw", "accept: ", throwable);
          ToastUtil.showShort("get history error, " + throwable.getMessage());
        }));
  }

  public void loadSearchData (String keyword, int page) {
    if (isAdded()) {

      mDisposable.add(GankRetrofit.getInstance()
          .getGankApi()
          .getSearchData(keyword, mCategory, 10, page)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(data -> {
            List<SearchResult> resultList = data.getSearchResults();
            if (resultList != null) {
              mRecyclerView.setAdapter(mViewAdapter);
              mViewAdapter.updateResult(resultList);
            }
          }, throwable -> {
            // TODO: 2018/8/15 show search error
            Toast.makeText(getContext(), "search error", Toast.LENGTH_SHORT).show();
            Log.e("zzw", "accept: +++" + throwable.getMessage());
          }));
    }
  }
}
