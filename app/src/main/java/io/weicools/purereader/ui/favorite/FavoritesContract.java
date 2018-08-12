package io.weicools.purereader.ui.favorite;

import io.weicools.purereader.data.GankContent;
import java.util.List;

import io.weicools.purereader.base.BasePresenter;
import io.weicools.purereader.base.BaseView;

/**
 * @author Weicools Create on 2018/4/15.
 * <p>
 * desc:
 */
public interface FavoritesContract {
  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void showFavorites(List<GankContent> dataList);

    void showNoData();
  }


  interface Presenter extends BasePresenter {
    void loadFavorites();
  }
}
