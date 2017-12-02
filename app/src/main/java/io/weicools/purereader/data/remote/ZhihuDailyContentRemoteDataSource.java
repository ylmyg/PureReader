package io.weicools.purereader.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.data.datasource.ZhihuDailyContentDataSource;
import io.weicools.purereader.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Main entry point for accessing the {@link ZhihuDailyNewsQuestion}s data.
 */

public class ZhihuDailyContentRemoteDataSource implements ZhihuDailyContentDataSource {
    @Nullable
    private static ZhihuDailyContentRemoteDataSource sInstance = null;

    private ZhihuDailyContentRemoteDataSource() {
    }

    public static ZhihuDailyContentRemoteDataSource getsInstance() {
        if (sInstance == null) {
            sInstance = new ZhihuDailyContentRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getZhihuDailyContent(int id, @NonNull final LoadZhihuDailyContentCallback contentCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.ZHIHU_DAILY_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService.ZhihuDailyService service = retrofit.create(RetrofitService.ZhihuDailyService.class);

        service.getZhihuContent(id)
                .enqueue(new Callback<ZhihuDailyContent>() {
                    @Override
                    public void onResponse(@NonNull Call<ZhihuDailyContent> call, @NonNull Response<ZhihuDailyContent> response) {
                        if (response.body() != null) {
                            contentCallback.onContentLoaded(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ZhihuDailyContent> call, @NonNull Throwable t) {
                        contentCallback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void saveContent(@NonNull ZhihuDailyContent content) {
        // Not required for the local data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }
}
