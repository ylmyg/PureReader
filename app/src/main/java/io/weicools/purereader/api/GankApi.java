package io.weicools.purereader.api;

import io.reactivex.Observable;
import io.weicools.purereader.data.DailyGankData;
import io.weicools.purereader.data.GankData;
import io.weicools.purereader.data.HistoryDate;
import io.weicools.purereader.data.SearchGankData;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Weicools Create on 2018/4/12.
 * <p>
 * desc: 干货集中营API: https://gank.io/api/
 */
public interface GankApi {
  String BASE_URL = "https://gank.io/api/";

  /**
   * https://gank.io/api/today
   *
   * @return Observable<DailyGankData>
   */
  @GET("today")
  Observable<DailyGankData> getLatestDailyData ();

  /**
   * https://gank.io/api/day/2018/08/09
   *
   * @param year y
   * @param month m
   * @param day d
   * @return Observable<DailyGankData>
   */
  @GET("day/{year}/{month}/{day}")
  Observable<DailyGankData> getDailyData (@Path("year") int year, @Path("month") int month, @Path("day") int day);

  /**
   * https://gank.io/api/data/Android/10/1
   *
   * @param category {all | Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App}
   * @param count count > 0
   * @param page page > 0
   * @return Observable<GankData>
   */
  @GET("data/{category}/{count}/{page}")
  Observable<GankData> getCategoryData (@Path("category") String category, @Path("count") int count,
      @Path("page") int page);

  /**
   * https://gank.io/api/search/query/listview/category/Android/count/10/page/1
   *
   * @param keyword keyword
   * @param category {all | Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App}
   * @param count max = 50
   * @param page page > 0
   * @return Observable<SearchGankData>
   */
  @GET("search/query/{keyword}/category/{category}/count/{count}/page/{page}")
  Observable<SearchGankData> getSearchData (@Path("keyword") String keyword, @Path("category") String category,
      @Path("count") int count, @Path("page") int page);

  /**
   * http://gank.io/api/day/history
   *
   * @return Observable<DailyGankData>
   */
  @GET("day/history")
  Observable<HistoryDate> getHistoryDate ();
}
