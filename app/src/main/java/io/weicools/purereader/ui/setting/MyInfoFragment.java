package io.weicools.purereader.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.weicools.purereader.R;

/**
 * @author Weicools Create on 2018.04.13
 *
 * desc:
 */
public class MyInfoFragment extends Fragment {
  public MyInfoFragment() {
    // Required empty public constructor
  }


  public static MyInfoFragment newInstance() {
    return new MyInfoFragment();
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_my_info, container, false);
    getChildFragmentManager().beginTransaction()
        .replace(R.id.info_container, new InfoPreferenceFragment())
        .commit();

    return view;
  }
}
