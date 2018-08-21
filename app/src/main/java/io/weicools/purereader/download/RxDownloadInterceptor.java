package io.weicools.purereader.download;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Weicools Create on 2018.08.21
 *
 * desc: 拦截器
 */
public class RxDownloadInterceptor implements Interceptor {
  private RxDownloadListener downloadListener;

  public RxDownloadInterceptor (RxDownloadListener downloadListener) {
    this.downloadListener = downloadListener;
  }

  @Override
  public Response intercept (Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    return response.newBuilder().body(new RxResponseBody(response.body(), downloadListener)).build();
  }
}
