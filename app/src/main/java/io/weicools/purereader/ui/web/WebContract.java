package io.weicools.purereader.ui.web;

import io.weicools.purereader.base.BasePresenter;
import io.weicools.purereader.base.BaseView;
import io.weicools.purereader.data.GankContent;

/**
 * @author Weicools Create on 2018.08.19
 *
 * desc:
 */
public interface WebContract {
  interface View extends BaseView<Presenter> {

    void showFavoriteSuccess ();

    void showFavoriteFailed ();

    void showUnFavoriteSuccess ();

    void showUnFavoriteFailed ();
  }

  interface Presenter extends BasePresenter {
    void favoriteContent (boolean isFavorite, GankContent content);
  }
}
