package io.weicools.purereader.ui.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import io.weicools.purereader.R;
import io.weicools.purereader.data.PostType;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.service.CacheService;
import io.weicools.purereader.ui.DatePickerDialog;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Main UI for the zhihu daily news screen.
 * Displays a grid of {@link ZhihuDailyNewsQuestion}s.
 */
public class ZhihuDailyFragment extends Fragment implements ZhihuDailyContract.View {
    private ZhihuDailyContract.Presenter mPresenter;

    // View references.
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private View mEmptyView;
    private FloatingActionButton fab;

    private ZhihuDailyNewsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mYear, mMonth, mDay;

    private boolean mIsFirstLoad = true;
    private int mListSize = 0;

    public ZhihuDailyFragment() {
        // Required empty public constructor
    }

    public static ZhihuDailyFragment newInstance() {
        return new ZhihuDailyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zhihu_daily, container, false);

        initView(view);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                mPresenter.loadNews(true, true, c.getTimeInMillis());
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mListSize - 1) {
                        loadMore();
                    }
                } else {
                    fab.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        c.set(mYear, mMonth, mDay);
        setLoadingIndicator(mIsFirstLoad);
        if (mIsFirstLoad) {
            mPresenter.loadNews(true, false, c.getTimeInMillis());
            mIsFirstLoad = false;
        } else {
            mPresenter.loadNews(false, false, c.getTimeInMillis());
        }
    }

    private void loadMore() {
        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, --mDay);
        mPresenter.loadNews(true, false, c.getTimeInMillis());
    }

    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, mDay);
//        DatePickerDialog dialog = DatePickerDialog.newInstance((new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//                mYear = year;
//                mMonth = monthOfYear;
//                mDay = dayOfMonth;
//                c.set(mYear, monthOfYear, mDay);
//
//                mPresenter.loadNews(true, true, c.getTimeInMillis());
//            }
//        }), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//
//        dialog.setMaxDate(Calendar.getInstance());
//
//        Calendar minDate = Calendar.getInstance();
//        minDate.set(2013, 5, 20);
//        dialog.setMinDate(minDate);
//        dialog.vibrate(false);

        DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                c.set(mYear, monthOfYear, mDay);

                mPresenter.loadNews(true, true, c.getTimeInMillis());
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dialog.show(getActivity().getFragmentManager(), ZhihuDailyFragment.class.getSimpleName());
    }

    @Override
    public void setPresenter(ZhihuDailyContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void initView(View view) {
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mRecyclerView = view.findViewById(R.id.rv_zhihu_news);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mEmptyView = view.findViewById(R.id.empty_view);
        fab = getActivity().findViewById(R.id.fab);
    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showResult(@NonNull List<ZhihuDailyNewsQuestion> list) {
        if (mAdapter == null) {
            mAdapter = new ZhihuDailyNewsAdapter(getContext(), list);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(list);
        }

        mListSize = list.size();
        Log.i("content size+++", mListSize + "");

        mEmptyView.setVisibility(list.isEmpty() ? View.VISIBLE : View.INVISIBLE);

        // Cache data of the items
        for (ZhihuDailyNewsQuestion item : list) {
            Intent intent = new Intent(CacheService.BROADCAST_FILTER_ACTION);
            intent.putExtra(CacheService.FLAG_ID, item.getId());
            intent.putExtra(CacheService.FLAG_TYPE, PostType.TYPE_ZHIHU);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        }
    }
}
