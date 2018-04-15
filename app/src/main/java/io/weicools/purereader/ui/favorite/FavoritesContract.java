package io.weicools.purereader.ui.favorite;

import java.util.List;

import io.weicools.purereader.BasePresenter;
import io.weicools.purereader.BaseView;
import io.weicools.purereader.data.GankData;

/**
 * Create by weicools on 2018/4/15.
 * <p>
 * desc:
 */

public interface FavoritesContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showFavorites(List<GankData> dataList);

        void showNoData();
    }

    interface Presenter extends BasePresenter {
        void loadFavorites();
    }
}
