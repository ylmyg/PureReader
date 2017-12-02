package io.weicools.purereader.retrofit;

import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.data.ZhihuDailyNews;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Interface of retrofit requests. API included.
 */

public interface RetrofitService {
    String ZHIHU_DAILY_BASE = "https://news-at.zhihu.com/api/4/news/";

    interface ZhihuDailyService {
        @GET("before/{date}")
        Call<ZhihuDailyNews> getZhihuList(@Path("date") String date);

        @GET("{id}")
        Call<ZhihuDailyContent> getZhihuContent(@Path("id") int id);
    }
}
