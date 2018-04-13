package io.weicools.purereader.module.gank;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.weicools.purereader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AndroidFragment extends Fragment {


    public AndroidFragment() {
        // Required empty public constructor
    }

    public static AndroidFragment newInstance() {
        return new AndroidFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_android, container, false);
    }

}
