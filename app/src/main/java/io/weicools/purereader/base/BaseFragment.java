package io.weicools.purereader.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.weicools.purereader.R;

/**
 * @author Weicools create on 2018.08.12
 *
 * desc:
 */
public abstract class BaseFragment extends Fragment {
  protected View mRootView;
  Unbinder unbinder;


  /**
   * get layout id
   *
   * @return layout id
   */
  protected abstract int getLayoutResId();


  @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mRootView = inflater.inflate(getLayoutResId(), container, false);
    unbinder = ButterKnife.bind(this, mRootView);
    return mRootView;
  }


  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }


  /**
   * Refresh
   */
  public void refresh() {
    // if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
    //   recyclerView.smoothScrollToPosition(0);
    // }
  }


  /**
   * Called when a fragment will be displayed
   */
  public void willBeDisplayed() {
    // Do what you want here, for example animate the content
    if (mRootView != null) {
      Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
      mRootView.startAnimation(fadeIn);
    }
  }


  /**
   * Called when a fragment will be hidden
   */
  public void willBeHidden() {
    if (mRootView != null) {
      Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
      mRootView.startAnimation(fadeOut);
    }
  }
}
