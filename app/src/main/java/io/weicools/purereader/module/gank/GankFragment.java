package io.weicools.purereader.module.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.weicools.purereader.R;
import io.weicools.purereader.data.GankData;
import io.weicools.purereader.module.LoadMoreRecyclerOnScrollListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GankFragment extends Fragment implements GankContract.View {
    private final static String ARG_CATEGORY = "arg_category";

    @BindView(R.id.rv_article)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    @Nullable
    private String lastId;
    private String category;
    private GankAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private GankContract.Presenter mPresenter;

    private int currPage = 1;
    //private int mListSize = 0;
    private boolean mIsFirstLoad = true;

    public static GankFragment newInstance(String categoryKey) {
        GankFragment fragment = new GankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, categoryKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gank, container, false);
        unbinder = ButterKnife.bind(this, view);

        new GankPresenter(this);
        mAdapter = new GankAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadGankData(true, category, 1);
            }
        });

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0 && mLayoutManager.findLastCompletelyVisibleItemPosition() == mListSize - 1) {
//                    mPresenter.loadGankData(false, category, ++currPage);
//                }
//            }
//        });
        mRecyclerView.addOnScrollListener(new LoadMoreRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                currPage = current_page;
                mPresenter.loadGankData(false, category, currPage);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLoadingIndicator(mIsFirstLoad);
        if (mIsFirstLoad) {
            mPresenter.loadGankData(false, category, 1);
            mIsFirstLoad = false;
        } else {
            //mPresenter.loadGankData(category, 10, currPage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.unSubscribe();
    }

    @Override
    public void setPresenter(GankContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showResult(List<GankData> dataList) {
        //mListSize = dataList.size();
        mAdapter.setDataList(dataList);
    }

    @Override
    public void updateResult(List<GankData> dataList) {
        //mListSize = dataList.size();
        mAdapter.updateData(dataList);
    }

    @Override
    public void showLoadingDataError() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoData() {
        mEmptyView.setVisibility(View.VISIBLE);
    }
}
