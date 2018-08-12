package io.weicools.purereader.ui.gank;

import android.util.Log;
import io.weicools.purereader.data.GankContent;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.api.GankRetrofit;

/**
 * @author Weicools Create on 2018/4/14.
 * <p>
 * desc:
 */

public class GankPresenter implements GankContract.Presenter {
  private static final String TAG = "GankPresenter";

  private GankContract.View mView;
  private CompositeDisposable mDisposable;


  public GankPresenter(GankContract.View view) {
    mView = view;
    mView.setPresenter(this);
    mDisposable = new CompositeDisposable();
  }


  @Override
  public void subscribe() {
    //loadGankData(1);
  }


  @Override
  public void unSubscribe() {
    mDisposable.clear();
  }


  @Override
  public void loadGankData(final boolean isRefresh, String category, int page) {
    mDisposable.add(GankRetrofit.getInstance().getGankApi()
        .getCategoryData(category, 10, page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(data -> {
          mView.setLoadingIndicator(false);
          List<GankContent> contentList = data.getResults();
          if (isRefresh) {
            mView.showResult(contentList);
          } else {
            mView.updateResult(contentList);
          }
        }, throwable -> {
          Log.e(TAG, "accept: error++" + throwable.getMessage());
          mView.showLoadingDataError();
        }));
  }


  @Override
  public void loadLatestDailyData() {
    mDisposable.add(GankRetrofit.getInstance().getGankApi()
        .getLatestDailyData()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(dailyGankData -> {
          List<GankContent> dataList = new ArrayList<>();
          dataList.addAll(dailyGankData.getResults().getAndroid());
          dataList.addAll(dailyGankData.getResults().getIOS());
          dataList.addAll(dailyGankData.getResults().getWebFont());
          dataList.addAll(dailyGankData.getResults().getApp());
          dataList.addAll(dailyGankData.getResults().getResource());
          mView.showResult(dataList);
          mView.setLoadingIndicator(false);
        }, throwable -> mView.showLoadingDataError()));
  }


  @Override
  public void loadDailyData(int year, int month, int day) {
    mDisposable.add(GankRetrofit.getInstance().getGankApi()
        .getDailyData(year, month, day)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(dailyGankData -> {
          List<GankContent> dataList = new ArrayList<>();
          dataList.addAll(dailyGankData.getResults().getAndroid());
          dataList.addAll(dailyGankData.getResults().getIOS());
          dataList.addAll(dailyGankData.getResults().getWebFont());
          dataList.addAll(dailyGankData.getResults().getApp());
          dataList.addAll(dailyGankData.getResults().getResource());
          mView.showResult(dataList);
          mView.setLoadingIndicator(false);
        }, throwable -> mView.showLoadingDataError()));
  }
}
