package io.weicools.purereader.util;

import android.content.Context;

/**
 * Create by weicools on 2018.08.16
 *
 * desc:
 */
public class UIUtils {
  public static int dip2px(Context context, float dp) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5);
  }
}
