package io.weicools.purereader.ui.timeline;

import android.support.annotation.NonNull;

import java.util.List;

import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.data.datasource.ZhihuDailyNewsDataSource;
import io.weicools.purereader.data.repository.ZhihuDailyNewsRepository;

/**
 * Create by weicools on 2017/12/3.
 * <p>
 * Listens to user actions from UI ({@link ZhihuDailyFragment}),
 * retrieves the data and update the ui as required.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {
    @NonNull
    private final ZhihuDailyContract.View mView;
    @NonNull
    private final ZhihuDailyNewsRepository mRepository;

    ZhihuDailyPresenter(@NonNull ZhihuDailyContract.View view,
                        @NonNull ZhihuDailyNewsRepository repository) {
        this.mView = view;
        this.mRepository = repository;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadNews(boolean forceUpdate, boolean cleanCache, long date) {
        mRepository.getZhihuDailyNews(forceUpdate, cleanCache, date, new ZhihuDailyNewsDataSource.LoadZhihuDailyNewsCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                if (mView.isActive()) {
                    mView.showResult(list);
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.setLoadingIndicator(false);
                }
            }
        });
    }
}
