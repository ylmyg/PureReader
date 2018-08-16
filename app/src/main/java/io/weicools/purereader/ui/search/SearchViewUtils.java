package io.weicools.purereader.ui.search;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import io.weicools.purereader.util.UIUtils;

/**
 * Create by weicools on 2018.08.16
 *
 * desc:
 */
public class SearchViewUtils {
  public static void handleToolBar (final Context context, final CardView search, final EditText editText) {
    //隐藏
    if (search.getVisibility() == View.VISIBLE) {
      final Animator animatorHide =
          ViewAnimationUtils.createCircularReveal(search, search.getWidth() - UIUtils.dip2px(context, 56),
              UIUtils.dip2px(context, 23),
              //确定元的半径（算长宽的斜边长，这样半径不会太短也不会很长效果比较舒服）
              (float) Math.hypot(search.getWidth(), search.getHeight()), 0);
      animatorHide.addListener(new Animator.AnimatorListener() {
        @Override public void onAnimationStart (Animator animation) { }

        @Override public void onAnimationEnd (Animator animation) {
          search.setVisibility(View.GONE);
          ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
              search.getWindowToken(), 0);
        }

        @Override public void onAnimationCancel (Animator animation) { }

        @Override public void onAnimationRepeat (Animator animation) { }
      });
      animatorHide.setDuration(300);
      animatorHide.start();
      editText.setText("");
      search.setEnabled(false);
    }
    //显示
    else {
      final Animator animator =
          ViewAnimationUtils.createCircularReveal(search, search.getWidth() - UIUtils.dip2px(context, 56),
              UIUtils.dip2px(context, 23), 0, (float) Math.hypot(search.getWidth(), search.getHeight()));
      animator.addListener(new Animator.AnimatorListener() {
        @Override public void onAnimationStart (Animator animation) { }

        @Override public void onAnimationEnd (Animator animation) {
          ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
              InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override public void onAnimationCancel (Animator animation) { }

        @Override public void onAnimationRepeat (Animator animation) { }
      });
      search.setVisibility(View.VISIBLE);
      if (search.getVisibility() == View.VISIBLE) {
        animator.setDuration(300);
        animator.start();
        search.setEnabled(true);
      }
    }
  }
}
