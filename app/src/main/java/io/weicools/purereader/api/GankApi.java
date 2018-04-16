package io.weicools.purereader.api;

import io.reactivex.Observable;
import io.weicools.purereader.data.DailyGankData;
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
    Observable<HttpResult<GankData>> getGankData(@Path("dataType") String dataType, @Path("count") int count, @Path("page") int page);

    /*
    搜索 API
    http://gank.io/api/search/query/listview/category/Android/count/10/page/1

    category 后面可接受参数 all | Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App
    count 最大 50
     */
    @GET("search/query/listview/category/{dataType}/count/{count}/page/{page}")
    Observable<HttpResult<GankData>> searchGankData(@Path("dataType") String dataType, @Path("count") int count, @Path("page") int page);

    /*
    每日数据： http://gank.io/api/day/年/月/日

    http://gank.io/api/day/2015/08/06
     */
    @GET("day/{year}/{month}/{day}")
    Observable<DailyGankData> getDailyGankData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    /*
    获取发过干货日期接口:
    http://gank.io/api/day/history 方式 GET
     */
    @GET("day/history")
    Observable<HttpResult<String>> getHistoryDate();
}
