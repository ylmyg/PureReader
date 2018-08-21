package io.weicools.purereader.download;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author Weicools Create on 2018.08.21
 *
 * desc: 重写ResponseBody，计算下载百分比
 */
public class RxResponseBody extends ResponseBody {
  private ResponseBody mResponseBody;

  private RxDownloadListener mListener;

  /**
   * BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
   */
  private BufferedSource bufferedSource;

  public RxResponseBody (ResponseBody responseBody, RxDownloadListener listener) {
    this.mResponseBody = responseBody;
    this.mListener = listener;
  }

  @Override
  public MediaType contentType () {
    return mResponseBody.contentType();
  }

  @Override
  public long contentLength () {
    return mResponseBody.contentLength();
  }

  @Override
  public BufferedSource source () {
    if (bufferedSource == null) {
      bufferedSource = Okio.buffer(source(mResponseBody.source()));
    }
    return bufferedSource;
  }

  private Source source (Source source) {
    return new ForwardingSource(source) {
      long totalBytesRead = 0L;

      @Override
      public long read (Buffer sink, long byteCount) throws IOException {
        long bytesRead = super.read(sink, byteCount);
        // read() returns the number of bytes read, or -1 if this source is exhausted.
        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
        if (null != mListener) {
          if (bytesRead != -1) {
            mListener.onProgress((int) (totalBytesRead * 100 / mResponseBody.contentLength()));
          }
        }
        return bytesRead;
      }
    };
  }
}
