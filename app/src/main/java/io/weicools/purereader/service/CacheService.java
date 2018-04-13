package io.weicools.purereader.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import io.weicools.purereader.data.PostType;
import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.database.AppDatabase;
import io.weicools.purereader.database.DatabaseCreator;
import io.weicools.purereader.api.RetrofitService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: weicools
 * Time: 2017/12/6 上午11:43
 */

public class CacheService extends Service {

    public static final String FLAG_ID = "flag_id";
    public static final String FLAG_TYPE = "flag_type";

    public static final String BROADCAST_FILTER_ACTION = "io.weicools.purereader.LOCAL_BROADCAST";

    private static final int MSG_CLEAR_CACHE_DONE = 1;

    @Nullable
    private AppDatabase mDatabase;
    private LocalReceiver mReceiver;

    private RetrofitService.ZhihuDailyService mZhihuDailyService;

    private boolean mZhihuCacheDone = false;

//    private final Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            switch (message.what) {
//                case MSG_CLEAR_CACHE_DONE:
//                    this.stopSelf();
//                    break;
//                default:
//                    break;
//            }
//            return true;
//        }
//    });

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new LocalReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_FILTER_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);

        mZhihuDailyService = new Retrofit.Builder()
                .baseUrl(RetrofitService.ZHIHU_DAILY_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService.ZhihuDailyService.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // DO NOT forget to unregister the receiver.
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Get zhihu content data by accessing network.
    private void cacheZhihuDailyContent(final int id) {
        if (mDatabase == null) {
            DatabaseCreator creator = DatabaseCreator.getInstance();
            if (!creator.isDatabaseCreated()) {
                creator.createDb(this);
            }
            mDatabase = creator.getDatabase();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDatabase != null && mDatabase.zhihuDailyContentDao().queryContentById(id) == null) {
                    mDatabase.beginTransaction();
                    // Call execute() rather than enqueue()
                    // or you will go back to main thread in onResponse() function.
                    try {
                        ZhihuDailyContent tmp = mZhihuDailyService.getZhihuContent(id).execute().body();
                        if (tmp != null) {
                            mDatabase.zhihuDailyContentDao().insert(tmp);
                            mDatabase.setTransactionSuccessful();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        mDatabase.endTransaction();
                        mZhihuCacheDone = true;
                        // TODO: 2017/12/6 clearTimeoutContent
//                        if (mDoubanCacheDone && mGuokrCacheDone) {
//                            clearTimeoutContent();
//                        }
                    }
                }
            }
        }).start();
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra(FLAG_ID, 0);
            @PostType
            int type = intent.getIntExtra(FLAG_TYPE, 0);

            switch (type) {
                case PostType.TYPE_ZHIHU:
                    cacheZhihuDailyContent(id);
                    break;
                default:
                    break;
            }
        }
    }
}
