package io.weicools.purereader.ui.gank;

import io.weicools.purereader.base.BasePresenter;
import io.weicools.purereader.base.BaseView;
import io.weicools.purereader.data.GankContent;
import java.util.List;

/**
 * @author Weicools Create on 2018/4/14.
 * <p>
 * desc:
 */
public interface GankContract {
  interface View extends BaseView<Presenter> {
    void setLoadingIndicator (boolean active);

    void showResult (List<GankContent> dataList);

    void updateResult (List<GankContent> dataList);

    void showLoadingDataError ();

    void showNoData ();
  }

  interface Presenter extends BasePresenter {
    void loadGankData (boolean isRefresh, String category, int page);

    void loadLatestDailyData ();

    void loadDailyData (int year, int month, int day);
  }
}
