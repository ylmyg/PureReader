package io.weicools.purereader.api;

import io.reactivex.Observable;
import io.weicools.purereader.data.GankData;
import io.weicools.purereader.data.HttpResult;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Create by weicools on 2018/4/12.
 * <p>
 * desc: 干货集中营API: http://gank.io/api/
 */

public interface GankApi {
    String BASE_URL = "http://gank.io/api/";

    /*
    分类数据: http://gank.io/api/data/数据类型/请求个数/第几页

    数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
    请求个数： 数字，大于0
    第几页：数字，大于0
     */
    @GET("data/{dataType}/{count}/{page}")
    Observable<HttpResult<GankData>> getAllGankData(@Path("dataType") String dataType, @Path("count") int count, @Path("page") int page);
}
