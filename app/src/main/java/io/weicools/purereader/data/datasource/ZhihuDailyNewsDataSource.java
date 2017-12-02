package io.weicools.purereader.data.datasource;

import android.support.annotation.NonNull;

import java.util.List;

import io.weicools.purereader.data.ZhihuDailyNewsQuestion;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Main entry point for accessing the {@link ZhihuDailyNewsQuestion}s data.
 */

public interface ZhihuDailyNewsDataSource {
    interface LoadZhihuDailyNewsCallback {
        void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list);

        void onDataNotAvailable();
    }

    interface GetNewsItemCallback {
        void onItemLoaded(@NonNull ZhihuDailyNewsQuestion item);

        void onDataNotAvailable();
    }

    void getZhihuDailyNews(boolean forceUpdate, boolean cleanCache, long date, @NonNull LoadZhihuDailyNewsCallback callback);

    void getFavorites(@NonNull LoadZhihuDailyNewsCallback callback);

    void getItem(int itemId, @NonNull GetNewsItemCallback callback);

    void favoriteItem(int itemId, boolean favorite);

    void saveAll(@NonNull List<ZhihuDailyNewsQuestion> list);
}
