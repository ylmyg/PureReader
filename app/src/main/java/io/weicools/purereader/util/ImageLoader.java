package io.weicools.purereader.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import io.weicools.purereader.PureReaderApp;

/**
 * @author Weicools create on 2018.08.12
 *
 * desc:
 */
public final class ImageLoader {
  private Context mContext;

  private ImageLoader () {
    mContext = PureReaderApp.getAppInstance();
  }

  private static class ImageLoaderHolder {
    static final ImageLoader INSTANCE = new ImageLoader();
  }

  public static ImageLoader getInstance () {
    return ImageLoaderHolder.INSTANCE;
  }

  public void loadImage (ImageView iv, String url, @DrawableRes int placeHolder) {
    RequestOptions options = new RequestOptions().centerCrop().placeholder(placeHolder);
    Glide.with(mContext).applyDefaultRequestOptions(options).load(url).into(iv);
  }
}