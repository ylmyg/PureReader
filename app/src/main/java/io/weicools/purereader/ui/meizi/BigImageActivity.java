package io.weicools.purereader.ui.meizi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.weicools.purereader.R;
import io.weicools.purereader.download.RxDownload;
import io.weicools.purereader.download.RxDownloadListener;
import io.weicools.purereader.util.ToastUtil;
import io.weicools.purereader.widget.RingProgressBar;

/**
 * @author Weicools Create on 2018.04.13
 *
 * desc:
 */
public class BigImageActivity extends AppCompatActivity {
  private static final String EXTRA_IMAGE_URL = "url";
  private static final String EXTRA_IMAGE_DESC = "desc";
  private static final int REQUEST_PERMISSION_CODE = 0x01;

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.draweeview) SimpleDraweeView mDraweeView;
  @BindView(R.id.ring_progress_bar) RingProgressBar mProgressBar;

  private String url, desc;
  private CompositeDisposable mDisposable;

  public static void startBigImageActivity (Context context, String url, String desc) {
    Intent intent = new Intent(context, BigImageActivity.class);
    intent.putExtra(BigImageActivity.EXTRA_IMAGE_URL, url);
    intent.putExtra(BigImageActivity.EXTRA_IMAGE_DESC, desc);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_big_image);
    ButterKnife.bind(this);
    mDisposable = new CompositeDisposable();

    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    url = getIntent().getStringExtra(BigImageActivity.EXTRA_IMAGE_URL);
    desc = getIntent().getStringExtra(BigImageActivity.EXTRA_IMAGE_DESC);
    GenericDraweeHierarchy hierarchy = mDraweeView.getHierarchy();
    hierarchy.setProgressBarImage(new ProgressBarDrawable());
    Uri uri = Uri.parse(url);
    mDraweeView.setImageURI(uri);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.menu_image, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
      case R.id.action_share:
        // TODO: 2018/8/21 share 
        break;
      case R.id.action_save:
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
          saveImage();
        } else {
          ActivityCompat.requestPermissions(this,
              new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
              REQUEST_PERMISSION_CODE);
        }
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_PERMISSION_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        saveImage();
      }
    } else {
      ToastUtil.showShort("没有赋予权限");
    }
  }

  @Override
  protected void onDestroy () {
    super.onDestroy();
    mDisposable.dispose();
  }

  private void saveImage () {
    RxDownload download = new RxDownload("https://ww1.sinaimg.cn/",new RxDownloadListener() {
      @Override
      public void onDownloadStart () {
        mProgressBar.setVisibility(View.VISIBLE);
      }

      @Override
      public void onDownloadFinish () { }

      @Override
      public void onProgress (int progress) {
        mProgressBar.setProgress(progress);
      }

      @Override
      public void onDownloadFail (String errorInfo) {
        mProgressBar.setVisibility(View.INVISIBLE);
        ToastUtil.showShort("保存失败啦…");
      }
    });

    String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/MeiziPicture";
    mDisposable.add(
        download.startDownload(url, downloadPath).observeOn(AndroidSchedulers.mainThread()).subscribe(inputStream -> {
          mProgressBar.setVisibility(View.INVISIBLE);
          ToastUtil.showShort("已经保存图片到SD卡目录下的MeiziPicture目录！");
        }, throwable -> {
          mProgressBar.setVisibility(View.INVISIBLE);
          ToastUtil.showShort("保存失败啦…");
        }));
  }
}
