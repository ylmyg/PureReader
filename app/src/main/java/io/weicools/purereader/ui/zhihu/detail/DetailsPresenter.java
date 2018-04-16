package io.weicools.purereader.ui.zhihu.detail;

import android.support.annotation.NonNull;

import io.weicools.purereader.R;
import io.weicools.purereader.data.ContentType;
import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.data.datasource.ZhihuDailyContentDataSource;
import io.weicools.purereader.data.repository.ZhihuDailyContentRepository;
import io.weicools.purereader.data.repository.ZhihuDailyNewsRepository;

/**
 * Author: weicools
 * Time: 2017/12/6 下午4:55
 * <p>
 * Listen to user actions from the UI ({@link DetailsActivity}),
 * retrieves the data and updates the UI as required.
 */

public class DetailsPresenter implements DetailsContract.Presenter {

    @NonNull
    private final DetailsContract.View mView;

    private ZhihuDailyNewsRepository mZhihuNewsRepository;
    private ZhihuDailyContentRepository mZhihuContentRepository;

    public DetailsPresenter(@NonNull DetailsContract.View view,
                            @NonNull ZhihuDailyNewsRepository zhihuNewsRepository,
                            @NonNull ZhihuDailyContentRepository zhihuContentRepository) {
        this.mView = view;
        mView.setPresenter(this);
        mZhihuNewsRepository = zhihuNewsRepository;
        mZhihuContentRepository = zhihuContentRepository;
    }

    @Override
    public void favorite(ContentType type, int id, boolean favorite) {
        if (type == ContentType.TYPE_ZHIHU_DAILY) {
            mZhihuNewsRepository.favoriteItem(id, favorite);
        }
    }

    @Override
    public void loadZhihuDailyContent(int id) {
        mZhihuContentRepository.getZhihuDailyContent(id, new ZhihuDailyContentDataSource.LoadZhihuDailyContentCallback() {
            @Override
            public void onContentLoaded(@NonNull ZhihuDailyContent content) {
                mView.showZhihuDailyContent(content);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showMessage(R.string.something_wrong);
            }
        });
    }

    @Override
    public void getLink(ContentType type, final int requestCode, int id) {
        switch (type) {
            case TYPE_ZHIHU_DAILY:
                mZhihuContentRepository.getZhihuDailyContent(id, new ZhihuDailyContentDataSource.LoadZhihuDailyContentCallback() {
                    @Override
                    public void onContentLoaded(@NonNull ZhihuDailyContent content) {
                        String url = content.getShareUrl();
                        if (requestCode == DetailsActivity.REQUEST_SHARE) {
                            mView.share(url);
                        } else if (requestCode == DetailsActivity.REQUEST_COPY_LINK) {
                            mView.copyLink(url);
                        } else if (requestCode == DetailsActivity.REQUEST_OPEN_WITH_BROWSER) {
                            mView.openWithBrowser(url);
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        mView.showMessage(R.string.share_failed);
                    }
                });
                break;
            default:
                break;
        }
    }
}
