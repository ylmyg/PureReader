package io.weicools.purereader.ui.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import io.weicools.purereader.R;

/**
 * @author Weicools Create on 2018.04.13
 *
 * desc:
 */
public class LicenseActivity extends AppCompatActivity {

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_license);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    WebView webView = findViewById(R.id.web_view);
    webView.loadUrl("file:///android_asset/licenses.html");
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return true;
  }
}
