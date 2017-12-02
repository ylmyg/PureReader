package io.weicools.purereader.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import io.weicools.purereader.data.ZhihuDailyNews;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.data.datasource.ZhihuDailyNewsDataSource;
import io.weicools.purereader.retrofit.RetrofitService;
import io.weicools.purereader.util.DateFormatUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Weicools on 2017/12/2.
 * <p>
 * Implementation of the {@link ZhihuDailyNews} data source that accesses network.
 */

public class ZhihuDailyNewsRemoteDataSource implements ZhihuDailyNewsDataSource {
    @Nullable
    private static ZhihuDailyNewsRemoteDataSource sInatance = null;

    private ZhihuDailyNewsRemoteDataSource() {
    }

    public static ZhihuDailyNewsRemoteDataSource getInstance() {
        if (sInatance == null) {
            sInatance = new ZhihuDailyNewsRemoteDataSource();
        }
        return sInatance;
    }

    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean cleanCache, long date, @NonNull final LoadZhihuDailyNewsCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.ZHIHU_DAILY_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService.ZhihuDailyService service = retrofit.create(RetrofitService.ZhihuDailyService.class);

        service.getZhihuList(DateFormatUtil.formatZhihuDailyDateLongToString(date))
                .enqueue(new Callback<ZhihuDailyNews>() {
                    @Override
                    public void onResponse(@NonNull Call<ZhihuDailyNews> call, @NonNull Response<ZhihuDailyNews> response) {
                        // Note: Only the timestamp of zhihu daily was set in remote source.
                        // The other two was set in repository due to structure of returning json.
                        long timestamp = DateFormatUtil.formatZhihuDailyDateStringToLong(response.body().getDate());

                        Log.d("TAG", "onResponse: timestamp " + timestamp);

                        if (response.body() != null) {
                            for (ZhihuDailyNewsQuestion item : response.body().getStories()) {
                                item.setTimestamp(timestamp);
                            }
                            callback.onNewsLoaded(response.body().getStories());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ZhihuDailyNews> call, @NonNull Throwable t) {
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getFavorites(@NonNull LoadZhihuDailyNewsCallback callback) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void getItem(int itemId, @NonNull GetNewsItemCallback callback) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void favoriteItem(int itemId, boolean favorite) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void saveAll(@NonNull List<ZhihuDailyNewsQuestion> list) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }
}
