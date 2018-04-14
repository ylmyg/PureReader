package io.weicools.purereader.module.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import io.weicools.purereader.BasePresenter;
import io.weicools.purereader.data.ContentType;
import io.weicools.purereader.data.ZhihuDailyContent;

/**
 * Author: weicools
 * Time: 2017/12/6 下午4:13
 */

public class DetailsContract {
    interface View {
        void setPresenter(Presenter presenter);

        void initView();

        void showMessage(@StringRes int stringRes);

        void showZhihuDailyContent(@NonNull ZhihuDailyContent content);

        void share(@Nullable String link);

        void copyLink(@Nullable String link);

        void openWithBrowser(@Nullable String link);
    }

    interface Presenter {
        void favorite(ContentType type, int id, boolean favorite);

        void loadZhihuDailyContent(int id);

        void getLink(ContentType type, int requestCode, int id);
    }
}
