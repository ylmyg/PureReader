package io.weicools.purereader.ui.gank;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.api.GankRetrofit;
import io.weicools.purereader.data.DailyGankData;
import io.weicools.purereader.data.GankData;
import io.weicools.purereader.data.HttpResult;

/**
 * Create by weicools on 2018/4/14.
 * <p>
 * desc:
 */

public class GankPresenter implements GankContract.Presenter {
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
        GankRetrofit.getInstance().getGankApi()
                .getGankData(category, 10, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult<List<GankData>>>() {
                    @Override
                    public void accept(HttpResult<List<GankData>> gankDataHttpResult) throws Exception {
                        mView.setLoadingIndicator(false);
                        List<GankData> dataList = gankDataHttpResult.getResults();
                        if (isRefresh) {
                            mView.showResult(dataList);
                        } else {
                            mView.updateResult(dataList);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showLoadingDataError();
                    }
                });
    }

    @Override
    public void loadDailyGankData() {
        GankRetrofit.getInstance().getGankApi()
                .getHistoryDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult<List<String>>>() {
                    @Override
                    public void accept(HttpResult<List<String>> listHttpResult) throws Exception {
                        String latestDate = listHttpResult.getResults().get(0);
                        String[] s = latestDate.split("-");
                        int year = Integer.parseInt(s[0]);
                        int month = Integer.parseInt(s[1]);
                        int day = Integer.parseInt(s[2]);
                        loadDailyData(year, month, day);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showLoadingDataError();
                    }
                });
    }

    @Override
    public void loadDailyData(int year, int month, int day) {
        GankRetrofit.getInstance().getGankApi()
                .getDailyGankData(year, month, day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DailyGankData>() {
                    @Override
                    public void accept(DailyGankData dailyGankData) throws Exception {
                        List<GankData> dataList = new ArrayList<>();
                        dataList.addAll(dailyGankData.getResults().androidList);
                        dataList.addAll(dailyGankData.getResults().iOSList);
                        dataList.addAll(dailyGankData.getResults().webFontList);
                        dataList.addAll(dailyGankData.getResults().appList);
                        dataList.addAll(dailyGankData.getResults().recommendList);
                        dataList.addAll(dailyGankData.getResults().girlList);
                        dataList.addAll(dailyGankData.getResults().videoList);
                        mView.showResult(dataList);
                        mView.setLoadingIndicator(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showLoadingDataError();
                    }
                });
    }
}
