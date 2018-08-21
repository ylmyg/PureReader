package io.weicools.purereader.download;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author Weicools Create on 2018.08.21
 *
 * desc:
 */
public class RxDownload {
  private static final String TAG = "RxDownload";
  private static final int DEFAULT_TIMEOUT = 15;

  private String mBaseUrl;
  private Retrofit mRetrofit;
  private RxDownloadListener mListener;

  public RxDownload (String baseUrl, RxDownloadListener listener) {
    mBaseUrl = baseUrl;
    this.mListener = listener;

    RxDownloadInterceptor interceptor = new RxDownloadInterceptor(mListener);
    OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor)
        .retryOnConnectionFailure(true)
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .build();

    mRetrofit = new Retrofit.Builder()
        .baseUrl(mBaseUrl)
        .client(httpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  /**
   * 开始下载
   */
  public Observable<InputStream> startDownload (@NonNull String url, final String filePath) {
    mListener.onDownloadStart();
    return mRetrofit.create(RxDownloadService.class)
        .download(url)
        .subscribeOn(Schedulers.io())
        .map(ResponseBody::byteStream)
        .observeOn(Schedulers.computation())
        .doOnNext(inputStream -> writeFile(inputStream, filePath));
  }

  /**
   * 将输入流写入文件
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void writeFile (InputStream inputString, String filePath) {

    File file = new File(filePath);
    if (!file.exists()) {
      file.mkdir();
    }

    FileOutputStream fos;
    try {
      int len;
      byte[] b = new byte[1024];
      fos = new FileOutputStream(file);
      while ((len = inputString.read(b)) != -1) {
        fos.write(b, 0, len);
      }
      inputString.close();
      fos.close();
    } catch (FileNotFoundException e) {
      mListener.onDownloadFail("FileNotFoundException");
    } catch (IOException e) {
      mListener.onDownloadFail("IOException");
    }
  }
}
