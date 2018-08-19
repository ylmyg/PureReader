package io.weicools.purereader.ui.web;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.database.ReaderDatabase;

/**
 * @author Weicools Create on 2018.08.19
 *
 * desc:
 */
public class WebPresenter implements WebContract.Presenter {
  private WebContract.View mView;
  private CompositeDisposable mDisposable;

  WebPresenter (WebContract.View view) {
    mView = view;
    mView.setPresenter(this);
    mDisposable = new CompositeDisposable();
  }

  @Override
  public void unSubscribe () {
    mDisposable.dispose();
  }

  @Override
  public void favoriteContent (boolean isFavorite, GankContent content) {
    if (isFavorite) {
      mDisposable.add(Completable.fromAction(() -> ReaderDatabase.getInstance().contentDao().deleteGankContent(content))
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(() -> mView.showUnFavoriteSuccess(), throwable -> mView.showUnFavoriteFailed()));
    } else {
      mDisposable.add(Completable.fromAction(() -> ReaderDatabase.getInstance().contentDao().insertGankContent(content))
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(() -> mView.showFavoriteSuccess(), throwable -> mView.showFavoriteFailed()));
    }
  }

  @Override
  public void isFavoriteContent (GankContent content) {
    mDisposable.add(ReaderDatabase.getInstance()
        .contentDao()
        .getFavorite(content.getId())
        .map(content1 -> content1 != null)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(isFavorite -> {
          if (isFavorite) {
            mView.showIsFavorite();
          }
        }));
  }
}
