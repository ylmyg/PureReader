package io.weicools.purereader.ui.favorite;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.database.ReaderDatabase;
import io.weicools.purereader.util.ToastUtil;

/**
 * @author Weicools Create on 2018/4/15.
 * <p>
 * desc:
 */
public class FavoritesPresenter implements FavoritesContract.Presenter {
  private FavoritesContract.View mView;
  private CompositeDisposable mDisposable;

  FavoritesPresenter (FavoritesContract.View view) {
    mView = view;
    mView.setPresenter(this);
    mDisposable = new CompositeDisposable();
  }

  @Override
  public void unSubscribe () {
    mDisposable.dispose();
  }

  @Override
  public void loadFavorites () {
    mDisposable.add(ReaderDatabase.getInstance()
        .contentDao()
        .getFavoriteList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(contentList -> {
          if (contentList.size() > 0) {
            mView.showFavorites(contentList);
          } else {
            mView.showNoData();
          }
        }, throwable -> {
          ToastUtil.showShort("load data error");
          mView.showNoData();
        }));
  }
}
