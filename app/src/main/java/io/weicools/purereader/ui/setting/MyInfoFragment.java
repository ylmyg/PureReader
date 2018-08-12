package io.weicools.purereader.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.weicools.purereader.R;
import io.weicools.purereader.base.BaseFragment;

/**
 * @author Weicools Create on 2018.04.13
 *
 * desc:
 */
public class MyInfoFragment extends BaseFragment {
  public MyInfoFragment() {
    // Required empty public constructor
  }


  public static MyInfoFragment newInstance() {
    return new MyInfoFragment();
  }


  @Override protected int getLayoutResId() {
    return R.layout.fragment_my_info;
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    getChildFragmentManager().beginTransaction()
        .replace(R.id.info_container, new InfoPreferenceFragment())
        .commit();
    return mRootView;
  }
}
