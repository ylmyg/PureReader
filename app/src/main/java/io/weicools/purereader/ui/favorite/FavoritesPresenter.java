package io.weicools.purereader.ui.favorite;

/**
 * @author Weicools Create on 2018/4/15.
 * <p>
 * desc:
 */
public class FavoritesPresenter implements FavoritesContract.Presenter {
  private FavoritesContract.View mView;


  public FavoritesPresenter(FavoritesContract.View view) {
    mView = view;
    mView.setPresenter(this);
  }


  @Override
  public void subscribe() {

  }


  @Override
  public void unSubscribe() {

  }


  @Override
  public void loadFavorites() {

  }
}
