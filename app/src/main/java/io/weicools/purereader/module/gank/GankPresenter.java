package io.weicools.purereader.module.gank;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.api.GankRetrofit;
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
    public void loadGankData(String category, int page) {
        GankRetrofit.getInstance().getGankApi()
                .getGankData(category, 10, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult<GankData>>() {
                    @Override
                    public void accept(HttpResult<GankData> gankDataHttpResult) throws Exception {
                        mView.setLoadingIndicator(false);
                        List<GankData> dataList = gankDataHttpResult.getResults();
                        mView.showResult(dataList);
                    }
                });
    }
}
