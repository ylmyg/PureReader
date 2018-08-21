package io.weicools.purereader.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Weicools Create on 2018.08.21
 *
 * desc: 网络请求service
 */
public interface RxDownloadService {
  @Streaming
  @GET
  Observable<ResponseBody> download (@Url String url);
}
