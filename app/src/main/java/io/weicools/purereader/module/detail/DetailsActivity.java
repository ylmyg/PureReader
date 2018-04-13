package io.weicools.purereader.module.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import io.weicools.purereader.R;
import io.weicools.purereader.data.ContentType;
import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.data.local.ZhihuDailyContentLocalDataSource;
import io.weicools.purereader.data.local.ZhihuDailyNewsLocalDataSource;
import io.weicools.purereader.data.remote.ZhihuDailyContentRemoteDataSource;
import io.weicools.purereader.data.remote.ZhihuDailyNewsRemoteDataSource;
import io.weicools.purereader.data.repository.ZhihuDailyContentRepository;
import io.weicools.purereader.data.repository.ZhihuDailyNewsRepository;
import io.weicools.purereader.util.InfoConstant;

/**
 * Author: weicools
 * Time: 2017/12/6 下午4:34
 * <p>
 * Activity for displaying the details of content.
 */

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View {
    private Context mContext;

    private Toolbar mToolbar;
    private ImageView mImageView;
    private WebView mWebView;
    private CollapsingToolbarLayout mToolbarLayout;
    private NestedScrollView mScrollView;

    private DetailsContract.Presenter mPresenter;

    private int mId;
    private ContentType mType;
    private String mTitle;

    private boolean mIsNightMode = false;
    private boolean mIsFavorite = false;

    public static int REQUEST_SHARE = 0;
    public static int REQUEST_COPY_LINK = 1;
    public static int REQUEST_OPEN_WITH_BROWSER = 2;

    public static final String KEY_ARTICLE_TYPE = "KEY_ARTICLE_TYPE";
    public static final String KEY_ARTICLE_ID = "KEY_ARTICLE_ID";
    public static final String KEY_ARTICLE_TITLE = "KEY_ARTICLE_TITLE";
    public static final String KEY_ARTICLE_IS_FAVORITE = "KEY_ARTICLE_IS_FAVORITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mContext = this;

        mId = getIntent().getIntExtra(KEY_ARTICLE_ID, -1);
        mType = (ContentType) getIntent().getSerializableExtra(KEY_ARTICLE_TYPE);
        mTitle = getIntent().getStringExtra(KEY_ARTICLE_TITLE);
        mIsNightMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(InfoConstant.KEY_NIGHT_MODE, false);
        mIsFavorite = getIntent().getBooleanExtra(KEY_ARTICLE_IS_FAVORITE, false);

        if (mType == ContentType.TYPE_ZHIHU_DAILY) {
            new DetailsPresenter(this,
                    ZhihuDailyNewsRepository.getInstance(ZhihuDailyNewsLocalDataSource.getInstance(DetailsActivity.this),
                            ZhihuDailyNewsRemoteDataSource.getInstance()),
                    ZhihuDailyContentRepository.getInstance(ZhihuDailyContentLocalDataSource.getInstance(DetailsActivity.this),
                            ZhihuDailyContentRemoteDataSource.getInstance()));
        }

        initView();

        setTitle(mTitle);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
        if (mType == ContentType.TYPE_ZHIHU_DAILY) {
            mPresenter.loadZhihuDailyContent(mId);
        }
        // TODO: 2017/12/6 load Douban Guokr
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_more) {

            final BottomSheetDialog dialog = new BottomSheetDialog(mContext);

            View view = getLayoutInflater().inflate(R.layout.actions_details_sheet, null);

            AppCompatTextView favorite = view.findViewById(R.id.text_view_favorite);
            AppCompatTextView copyLink = view.findViewById(R.id.text_view_copy_link);
            AppCompatTextView openWithBrowser = view.findViewById(R.id.text_view_open_with_browser);
            AppCompatTextView share = view.findViewById(R.id.text_view_share);

            if (mIsFavorite) {
                favorite.setText(R.string.unfavorite);
            } else {
                favorite.setText(R.string.favorite);
            }

            // add to bookmarks or delete from bookmarks
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    mIsFavorite = !mIsFavorite;
                    mPresenter.favorite(mType, mId, mIsFavorite);
                }
            });

            // copy the article's link to clipboard
            copyLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.getLink(mType, REQUEST_COPY_LINK, mId);
                    dialog.dismiss();
                }
            });

            // open the link in system browser
            openWithBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.getLink(mType, REQUEST_OPEN_WITH_BROWSER, mId);
                    dialog.dismiss();
                }
            });

            // getLink the content as text
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.getLink(mType, REQUEST_SHARE, mId);
                    dialog.dismiss();
                }
            });

            dialog.setContentView(view);
            dialog.show();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZhihuDailyContentRepository.destroyInstance();
    }

    @Override
    public void setPresenter(DetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initView() {
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mImageView = findViewById(R.id.image_view);

        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mScrollView = findViewById(R.id.nested_scroll_view);

        mWebView = findViewById(R.id.web_view);

        mWebView.setScrollbarFadingEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);

        // Show the images or not.
        mWebView.getSettings().setBlockNetworkImage(PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(InfoConstant.KEY_NO_IMG_MODE, false));

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                CustomTabsHelper.openUrl(mContext, url);
                return true;
            }
        });
    }

    @Override
    public void showMessage(int stringRes) {
        Toast.makeText(mContext, stringRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showZhihuDailyContent(@NonNull ZhihuDailyContent content) {
        if (content.getBody() != null) {
            String result = content.getBody();
            result = result.replace("<div class=\"img-place-holder\">", "");
            result = result.replace("<div class=\"headline\">", "");

            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";

            String theme = "<body className=\"\" onload=\"onLoaded()\">";
            if (mIsNightMode) {
                theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
            }

            result = "<!DOCTYPE html>\n"
                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                    + "<head>\n"
                    + "\t<meta charset=\"utf-8\" />"
                    + css
                    + "\n</head>\n"
                    + theme
                    + result
                    + "</body></html>";

            mWebView.loadDataWithBaseURL("x-data://base", result, "text/html", "utf-8", null);
        } else {
            mWebView.loadDataWithBaseURL("x-data://base", content.getShareUrl(), "text/html", "utf-8", null);
        }

        setCover(content.getImage());
    }

    @Override
    public void share(@Nullable String link) {
        try {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String shareText = "" + mTitle + " " + link;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex) {
            showMessage(R.string.something_wrong);
        }
    }

    @Override
    public void copyLink(@Nullable String link) {
        if (link != null) {
            ClipboardManager manager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", Html.fromHtml(link).toString());
            manager.setPrimaryClip(clipData);
            showMessage(R.string.copied_to_clipboard);
        } else {
            showMessage(R.string.something_wrong);
        }
    }

    @Override
    public void openWithBrowser(@Nullable String link) {
        if (link != null) {
            CustomTabsHelper.openUrl(mContext, link);
        } else {
            showMessage(R.string.something_wrong);
        }
    }

    private void setTitle(@NonNull String title) {
        setCollapsingToolbarLayoutTitle(title);
    }

    private void setCover(@Nullable String url) {
        if (url != null) {
            Glide.with(mContext)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .into(mImageView);
        } else {
            mImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    // to change the title's font size of toolbar layout
    private void setCollapsingToolbarLayoutTitle(String title) {
        mToolbarLayout.setTitle(title);
        mToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }
}
