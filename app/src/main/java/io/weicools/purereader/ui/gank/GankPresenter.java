package io.weicools.purereader.ui.gank;

import android.util.Log;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.api.GankRetrofit;
import io.weicools.purereader.data.DailyGankData;
import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018/4/14.
 * <p>
 * desc:
 */

public class GankPresenter implements GankContract.Presenter {
  private static final String TAG = "GankPresenter";

  private GankContract.View mView;
  private CompositeDisposable mDisposable;

  public GankPresenter (GankContract.View view) {
    mView = view;
    mView.setPresenter(this);
    mDisposable = new CompositeDisposable();
  }

  @Override
  public void unSubscribe () {
    mDisposable.dispose();
  }

  @Override
  public void loadGankData (final boolean isRefresh, String category, int page) {
    mDisposable.add(GankRetrofit.getInstance().getGankApi().getCategoryData(category, 10, page).map(data -> {
      List<GankContent> contentList = data.getResults();
      for (GankContent content : contentList) {
        content.setPublishedAt(DateTimeUtil.dateFormat(content.getPublishedAt(), DateTimeUtil.DATE_FORMAT_STYLE5,
            DateTimeUtil.DATE_FORMAT_STYLE4));
      }
      return contentList;
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(contentList -> {
      mView.setLoadingIndicator(false);
      if (isRefresh) {
        mView.showResult(contentList);
      } else {
        mView.updateResult(contentList);
      }
    }, throwable -> {
      Log.e(TAG, "accept: error++" + throwable.getMessage());
      mView.setLoadingIndicator(false);
      mView.showLoadingDataError();
    }));
  }

  @Override
  public void loadLatestDailyData () {
    mDisposable.add(GankRetrofit.getInstance()
        .getGankApi()
        .getLatestDailyData()
        .map(this::getContentList)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(dataList -> {
          mView.setLoadingIndicator(false);
          mView.showResult(dataList);
        }, throwable -> {
          mView.setLoadingIndicator(false);
          mView.showNoData();
        }));
  }

  @Override
  public void loadDailyData (int year, int month, int day) {
    mDisposable.add(GankRetrofit.getInstance()
        .getGankApi()
        .getDailyData(year, month, day)
        .map(this::getContentList)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(dataList -> {
          mView.setLoadingIndicator(false);
          if (dataList.size() == 0) {
            mView.showNoData();
          } else {
            mView.showResult(dataList);
          }
        }, throwable -> {
          mView.setLoadingIndicator(false);
          mView.showLoadingDataError();
        }));
  }

  private List<GankContent> getContentList (DailyGankData dailyData) {
    List<GankContent> dataList = new ArrayList<>();
    List<GankContent> androidList = dailyData.getResults().getAndroid();
    List<GankContent> iOSList = dailyData.getResults().getIOS();
    List<GankContent> webFontList = dailyData.getResults().getWebFont();
    List<GankContent> appList = dailyData.getResults().getApp();
    List<GankContent> resourceList = dailyData.getResults().getResource();
    if (androidList != null) {
      dataList.addAll(androidList);
    }
    if (iOSList != null) {
      dataList.addAll(iOSList);
    }
    if (webFontList != null) {
      dataList.addAll(webFontList);
    }
    if (appList != null) {
      dataList.addAll(appList);
    }
    if (resourceList != null) {
      dataList.addAll(resourceList);
    }
    for (GankContent content : dataList) {
      content.setPublishedAt(DateTimeUtil.dateFormat(content.getPublishedAt(), DateTimeUtil.DATE_FORMAT_STYLE5,
          DateTimeUtil.DATE_FORMAT_STYLE4));
    }
    return dataList;
  }
}
