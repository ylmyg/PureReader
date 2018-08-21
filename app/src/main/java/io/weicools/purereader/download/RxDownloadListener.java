package io.weicools.purereader.download;

/**
 * @author Weicools Create on 2018.08.21
 *
 * desc: 下载回调
 */
public interface RxDownloadListener {
  void onDownloadStart ();

  void onDownloadFinish ();

  void onProgress (int progress);

  void onDownloadFail (String errorInfo);
}
