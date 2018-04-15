package io.weicools.purereader.ui.zhihu;

import android.support.annotation.NonNull;

import java.util.List;

import io.weicools.purereader.base.BaseView;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;

/**
 * Create by weicools on 2017/12/3.
 * <p>
 * This specifies the contract between the view and the presenter.
 */

public interface ZhihuDailyContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void setLoadingIndicator(boolean active);

        void showResult(@NonNull List<ZhihuDailyNewsQuestion> list);
    }

    interface Presenter {
        void loadNews(boolean forceUpdate, boolean cleanCache, long date);
    }
}
