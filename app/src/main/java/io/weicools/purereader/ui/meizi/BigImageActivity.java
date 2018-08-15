package io.weicools.purereader.ui.meizi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.weicools.purereader.R;
import io.weicools.purereader.util.SaveImageUtil;
import io.weicools.purereader.util.ShareUtil;

/**
 * @author Weicools Create on 2018.04.13
 *
 * desc:
 */
public class BigImageActivity extends AppCompatActivity {
  public static final String EXTRA_IMAGE_URL = "url";
  public static final String EXTRA_IMAGE_DESC = "desc";
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.draweeview) SimpleDraweeView mDraweeview;

  private String url, desc;

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

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    url = getIntent().getStringExtra(BigImageActivity.EXTRA_IMAGE_URL);
    desc = getIntent().getStringExtra(BigImageActivity.EXTRA_IMAGE_DESC);
    GenericDraweeHierarchy hierarchy = mDraweeview.getHierarchy();
    hierarchy.setProgressBarImage(new ProgressBarDrawable());
    Uri uri = Uri.parse(url);
    mDraweeview.setImageURI(uri);
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
        ShareUtil.shareImage(this, SaveImageUtil.saveImage(this, url, desc));
        break;
      case R.id.action_save:
        SaveImageUtil.saveImage(this, url, desc);
        Toast.makeText(this, "已经保存图片啦", Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
