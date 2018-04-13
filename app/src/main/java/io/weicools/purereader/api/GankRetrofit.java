package io.weicools.purereader.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by weicools on 2018/4/12.
 * <p>
 * desc:
 */

public class GankRetrofit {
    private GankApi mGankApi;

    private GankRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GankApi.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mGankApi = retrofit.create(GankApi.class);
    }

    private static class WanRetrofitHolder {
        static final GankRetrofit INSTANCE = new GankRetrofit();
    }

    public static GankRetrofit getInstance() {
        return WanRetrofitHolder.INSTANCE;
    }

    public GankApi getGankApi() {
        return mGankApi;
    }
}
