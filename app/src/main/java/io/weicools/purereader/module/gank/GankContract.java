package io.weicools.purereader.module.gank;

import java.util.List;

import io.weicools.purereader.BasePresenter;
import io.weicools.purereader.BaseView;
import io.weicools.purereader.data.GankData;

/**
 * Create by weicools on 2018/4/14.
 * <p>
 * desc:
 */

public interface GankContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showResult(List<GankData> dataList);
        void updateResult(List<GankData> dataList);

        void showLoadingDataError();

        void showNoData();
    }

    interface Presenter extends BasePresenter {
        void loadGankData(boolean isRefresh, String category, int page);
    }
}
