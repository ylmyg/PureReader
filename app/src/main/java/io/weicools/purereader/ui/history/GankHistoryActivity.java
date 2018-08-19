package io.weicools.purereader.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.R;
import io.weicools.purereader.api.GankRetrofit;
import java.util.ArrayList;

/**
 * @author weicools
 */
public class GankHistoryActivity extends AppCompatActivity {
  public static final String ARG_DATE = "date";
  public static final int REQUEST_DATE_CODE = 1;

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.rv_history) RecyclerView rvHistory;
  @BindView(R.id.empty_view) LinearLayout emptyView;

  private GankHistoryAdapter mAdapter;
  private CompositeDisposable mDisposable;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gank_history);
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    mDisposable = new CompositeDisposable();
    mAdapter = new GankHistoryAdapter(this, date -> {
      Intent i = getIntent();
      i.putExtra(ARG_DATE, date);
      setResult(RESULT_OK, i);
      finish();
    });
    rvHistory.setLayoutManager(new LinearLayoutManager(this));
    rvHistory.setAdapter(mAdapter);

    loadGankDate();
  }

  private void loadGankDate () {
    mDisposable.add(GankRetrofit.getInstance()
        .getGankApi()
        .getHistoryDate()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(historyDate -> {
          emptyView.setVisibility(View.INVISIBLE);
          mAdapter.setDateList(historyDate.getResults());
        }, throwable -> {
          emptyView.setVisibility(View.INVISIBLE);
          mAdapter.setDateList(new ArrayList<>());
        }));
  }

  @Override
  protected void onDestroy () {
    super.onDestroy();
    mDisposable.dispose();
  }
}
