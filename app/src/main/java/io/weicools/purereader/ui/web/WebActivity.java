package io.weicools.purereader.ui.web;

import android.annotation.SuppressLint;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.weicools.purereader.R;
import io.weicools.purereader.ui.CustomTabsHelper;
import io.weicools.purereader.util.ImageLoader;
import io.weicools.purereader.util.InfoConstant;
import io.weicools.purereader.util.ToastUtil;

/**
 * @author weicools
 */
public class WebActivity extends AppCompatActivity {

  @BindView(R.id.image_view) ImageView mImageView;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
  @BindView(R.id.web_view) WebView mWebView;
  @BindView(R.id.nested_scroll_view) NestedScrollView mNestedScrollView;

  private Context mContext;
  private String mUrl, mDesc, mImgUrl;
  private static final String URL = "url";
  private static final String DESC = "desc";
  private static final String IMG_URL = "imgUrl";

  private boolean mIsNightMode = false;
  private boolean mIsFavorite = false;

  public static void startWebActivity (Context context, String url, String desc, String imgUrl) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra(URL, url);
    intent.putExtra(DESC, desc);
    intent.putExtra(IMG_URL, imgUrl);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    ButterKnife.bind(this);
    mContext = this;

    mUrl = getIntent().getStringExtra(URL);
    mDesc = getIntent().getStringExtra(DESC);
    mImgUrl = getIntent().getStringExtra(IMG_URL);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setToolbarTitle(mDesc);
    setCover(mImgUrl);
    mToolbar.setOnClickListener(view -> mNestedScrollView.smoothScrollTo(0, 0));
    initWebView();
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void initWebView () {
    mWebView.setScrollbarFadingEnabled(true);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.getSettings().setBuiltInZoomControls(false);
    mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    mWebView.getSettings().setDomStorageEnabled(true);
    mWebView.getSettings().setAppCacheEnabled(false);

    // Show the images or not.
    mWebView.getSettings()
        .setBlockNetworkImage(
            PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(InfoConstant.KEY_NO_IMG_MODE, false));

    mWebView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading (WebView view, String url) {
        CustomTabsHelper.openUrl(mContext, url);
        return true;
      }
    });

    //CustomTabsHelper.openUrl(mContext, mUrl);
    mWebView.loadUrl(mUrl);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.menu_more, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
    } else if (id == R.id.action_more) {
      final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
      View view = getLayoutInflater().inflate(R.layout.actions_details_sheet, null);
      AppCompatTextView favorite = view.findViewById(R.id.text_view_favorite);
      AppCompatTextView copyLink = view.findViewById(R.id.text_view_copy_link);
      final AppCompatTextView openWithBrowser = view.findViewById(R.id.text_view_open_with_browser);
      AppCompatTextView share = view.findViewById(R.id.text_view_share);

      if (mIsFavorite) {
        favorite.setText(R.string.unfavorite);
      } else {
        favorite.setText(R.string.favorite);
      }

      // add to bookmarks or delete from bookmarks
      favorite.setOnClickListener(view1 -> {
        dialog.dismiss();
        mIsFavorite = !mIsFavorite;
        //mPresenter.favorite(mType, mId, mIsFavorite);
      });

      // copy the article's link to clipboard
      copyLink.setOnClickListener(view12 -> {
        //mPresenter.getLink(mType, REQUEST_COPY_LINK, mId);
        copyLink(mUrl);
        dialog.dismiss();
      });

      // open the link in system browser
      openWithBrowser.setOnClickListener(view13 -> {
        //mPresenter.getLink(mType, REQUEST_OPEN_WITH_BROWSER, mId);
        openWithBrowser(mUrl);
        dialog.dismiss();
      });

      // getLink the content as text
      share.setOnClickListener(view14 -> {
        //mPresenter.getLink(mType, REQUEST_SHARE, mId);
        share(mUrl);
        dialog.dismiss();
      });

      dialog.setContentView(view);
      dialog.show();
    }
    return true;
  }

  private void setToolbarTitle (@NonNull String title) {
    mToolbarLayout.setTitle(title);
    mToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
    mToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    mToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
    mToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
  }

  private void setCover (@Nullable String url) {
    if (!TextUtils.isEmpty(url)) {
      ImageLoader.getInstance().loadImage(mImageView, url, R.drawable.placeholder);
    } else {
      mImageView.setImageResource(R.drawable.placeholder);
    }
  }

  private void share (@Nullable String link) {
    try {
      Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
      String shareText = "" + mDesc + " " + link;
      shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
      startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
    } catch (android.content.ActivityNotFoundException ex) {
      ToastUtil.showShort(R.string.something_wrong);
    }
  }

  private void copyLink (@Nullable String link) {
    if (link != null) {
      ClipboardManager manager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
      ClipData clipData = ClipData.newPlainText("text", Html.fromHtml(link).toString());
      if (manager != null) {
        manager.setPrimaryClip(clipData);
      }
      ToastUtil.showShort(R.string.copied_to_clipboard);
    } else {
      ToastUtil.showShort(R.string.something_wrong);
    }
  }

  private void openWithBrowser (@Nullable String link) {
    if (link != null) {
      CustomTabsHelper.openUrl(mContext, link);
    } else {
      ToastUtil.showShort(R.string.something_wrong);
    }
  }
}
