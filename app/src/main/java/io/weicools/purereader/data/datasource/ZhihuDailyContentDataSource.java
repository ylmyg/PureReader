package io.weicools.purereader.data.datasource;

import android.support.annotation.NonNull;

import io.weicools.purereader.data.ZhihuDailyContent;

/**
 * Created by Weicools on 2017/12/2.
 * <p>
 * Main entry point for accessing the {@link ZhihuDailyContent} data.
 */

public interface ZhihuDailyContentDataSource {

    interface LoadZhihuDailyContentCallback {
        void onContentLoaded(@NonNull ZhihuDailyContent content);

        void onDataNotAvailable();
    }

    void getZhihuDailyContent(int id, @NonNull LoadZhihuDailyContentCallback contentCallback);

    void saveContent(@NonNull ZhihuDailyContent content);
}
