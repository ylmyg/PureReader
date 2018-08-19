package io.weicools.purereader.ui.web;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.weicools.purereader.R;
import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.ui.CustomTabsHelper;
import io.weicools.purereader.util.InfoConstant;
import io.weicools.purereader.util.ToastUtil;

/**
 * @author weicools
 */
public class WebActivity extends AppCompatActivity implements WebContract.View {
  private static final String ARG_IS_FAVORITE = "is_favorite";
  private static final String ARG_GANK_CONTENT = "gank_content";

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.web_view) WebView mWebView;
  @BindView(R.id.nested_scroll_view) NestedScrollView mNestedScrollView;
  private MenuItem favoriteItem;

  private WebContract.Presenter mPresenter;
  private Context mContext;
  private GankContent mContent;
  private String mUrl, mDesc;
  private boolean mIsFavorite;

  public static void startWebActivity (Context context, GankContent content, boolean isFavorite) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra(ARG_GANK_CONTENT, content);
    intent.putExtra(ARG_IS_FAVORITE, isFavorite);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    ButterKnife.bind(this);
    mContext = this;

    mContent = (GankContent) getIntent().getSerializableExtra(ARG_GANK_CONTENT);
    mIsFavorite = getIntent().getBooleanExtra(ARG_IS_FAVORITE, false);
    mUrl = mContent.getUrl();
    mDesc = mContent.getDesc();

    mToolbar.setTitle(mDesc);
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    initWebView();
    new WebPresenter(this);
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
      public boolean shouldOverrideUrlLoading (WebView view, WebResourceRequest request) {
        CustomTabsHelper.openUrl(mContext, request.getUrl().toString());
        return true;
      }
    });

    mWebView.loadUrl(mUrl);
  }

  @Override
  public void setPresenter (WebContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.menu_more, menu);
    favoriteItem = menu.getItem(0);
    if (mIsFavorite) {
      favoriteItem.setIcon(R.drawable.ic_favorite);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
    } else if (id == R.id.action_favorite) {
      mPresenter.favoriteContent(mIsFavorite, mContent);
    } else if (id == R.id.action_more) {
      showBottomDialog();
    }
    return true;
  }

  private void showBottomDialog () {
    final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
    View view = getLayoutInflater().inflate(R.layout.actions_details_sheet, null);
    AppCompatTextView copyLink = view.findViewById(R.id.text_view_copy_link);
    final AppCompatTextView openWithBrowser = view.findViewById(R.id.text_view_open_with_browser);
    AppCompatTextView share = view.findViewById(R.id.text_view_share);

    // copy the article's link to clipboard
    copyLink.setOnClickListener(v -> {
      copyLink(mUrl);
      dialog.dismiss();
    });

    // open the link in system browser
    openWithBrowser.setOnClickListener(v -> {
      openWithBrowser(mUrl);
      dialog.dismiss();
    });

    // getLink the content as text
    share.setOnClickListener(v -> {
      share(mUrl);
      dialog.dismiss();
    });

    dialog.setContentView(view);
    dialog.show();
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
      ClipData clipData = ClipData.newPlainText("text", link);
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

  @Override
  public void showFavoriteSuccess () {
    favoriteItem.setIcon(R.drawable.ic_favorite);
    ToastUtil.showShort("favorite success");
  }

  @Override
  public void showFavoriteFailed () {
    ToastUtil.showShort("favorite failed");
  }

  @Override
  public void showUnFavoriteSuccess () {
    favoriteItem.setIcon(R.drawable.ic_favorite_border);
    ToastUtil.showShort("un favorite success");
  }

  @Override
  public void showUnFavoriteFailed () {
    ToastUtil.showShort("un favorite failed");
  }
}
