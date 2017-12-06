package io.weicools.purereader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.data.datasource.ZhihuDailyContentDataSource;
import io.weicools.purereader.data.local.ZhihuDailyContentLocalDataSource;
import io.weicools.purereader.data.remote.ZhihuDailyContentRemoteDataSource;

/**
 * Author: weicools
 * Time: 2017/12/6 下午5:04
 * <p>
 * Concrete implementation to load {@link ZhihuDailyContent} from the data sources into a cache.
 * <p>
 * Use the remote data source firstly, which is obtained from the server.
 * If the remote data was not available, then use the local data source,
 * which was from the locally persisted in database.
 */

public class ZhihuDailyContentRepository implements ZhihuDailyContentDataSource {

    @NonNull
    private final ZhihuDailyContentDataSource mLocalDataSource;
    @NonNull
    private final ZhihuDailyContentDataSource mRemoteDataSource;

    @Nullable
    private ZhihuDailyContent mContent;

    @Nullable
    private static ZhihuDailyContentRepository sInstance = null;

    private ZhihuDailyContentRepository(@NonNull ZhihuDailyContentLocalDataSource localDataSource,
                                        @NonNull ZhihuDailyContentRemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static ZhihuDailyContentRepository getInstance(@NonNull ZhihuDailyContentLocalDataSource localDataSource,
                                                          @NonNull ZhihuDailyContentRemoteDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new ZhihuDailyContentRepository(localDataSource, remoteDataSource);
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public void getZhihuDailyContent(final int id, @NonNull final LoadZhihuDailyContentCallback callback) {
        if (mContent != null) {
            callback.onContentLoaded(mContent);
            return;
        }

        mRemoteDataSource.getZhihuDailyContent(id, new LoadZhihuDailyContentCallback() {
            @Override
            public void onContentLoaded(@NonNull ZhihuDailyContent content) {
                if (mContent == null) {
                    mContent = content;
                    saveContent(content);
                }
                callback.onContentLoaded(content);
            }

            @Override
            public void onDataNotAvailable() {
                mLocalDataSource.getZhihuDailyContent(id, new LoadZhihuDailyContentCallback() {
                    @Override
                    public void onContentLoaded(@NonNull ZhihuDailyContent content) {
                        if (mContent == null) {
                            mContent = content;
                        }
                        callback.onContentLoaded(content);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveContent(@NonNull ZhihuDailyContent content) {
        // Note: Setting of timestamp was done in the {@link ZhihuDailyContentLocalDataSource} class.
        mLocalDataSource.saveContent(content);
        mRemoteDataSource.saveContent(content);
    }
}
