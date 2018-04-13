package io.weicools.purereader.module.gank;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.weicools.purereader.R;
import io.weicools.purereader.api.GankRetrofit;
import io.weicools.purereader.data.GankData;
import io.weicools.purereader.data.HttpResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GankFragment extends Fragment {

    @BindView(R.id.rv_article)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private GankAdapter mAdapter;

    public GankFragment() {
        // Required empty public constructor
    }

    public static GankFragment newInstance() {
        return new GankFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gank, container, false);
        unbinder = ButterKnife.bind(this, view);

        mAdapter = new GankAdapter(getContext());
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadArticles(1);
            }
        });

        return view;
    }

    private void loadArticles(int i) {
        GankRetrofit.getInstance().getGankApi()
                .getAllGankData("all", 10, i)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult<GankData>>() {
                    @Override
                    public void accept(HttpResult<GankData> gankDataHttpResult) throws Exception {
                        List<GankData> dataList = gankDataHttpResult.getResults();
                        mAdapter.setDataList(dataList);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadArticles(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
