package io.weicools.purereader.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import io.weicools.purereader.R;
import io.weicools.purereader.ui.setting.LicenseActivity;

/**
 * @author Weicools Create on 2018.08.14
 *
 * desc:
 */
public class AboutActivity extends MaterialAboutActivity {
  @SuppressWarnings("SpellCheckingInspection")
  @NonNull @Override
  protected MaterialAboutList getMaterialAboutList(@NonNull final Context c) {
    MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder()
        .addItem(new MaterialAboutTitleItem.Builder()
            .text("Pure Reader")
            .desc("© 2018 Weicools")
            .icon(R.mipmap.ic_launcher)
            .build())
        .addItem(ConvenienceBuilder.createVersionActionItem(
            c, ContextCompat.getDrawable(c, R.drawable.ic_update),
            "Version", false))
        .addItem(new MaterialAboutActionItem.Builder()
            .text("Licenses")
            .icon(R.drawable.ic_license)
            .setOnClickAction(() -> c.startActivity(new Intent(c, LicenseActivity.class)))
            .build())
        .addItem(new MaterialAboutActionItem.Builder()
            .text("Changelog")
            .icon(ContextCompat.getDrawable(c, R.drawable.ic_track_changes))
            .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Releases", "https://github.com/lecymeng/PureReader/releases", true, false))
            .build())
        .addItem(ConvenienceBuilder.createRateActionItem(
            c, ContextCompat.getDrawable(c, R.drawable.ic_star_filled),
            "Rate this app", null));

    MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder()
        .title("Author")
        .addItem(new MaterialAboutActionItem.Builder()
            .text("Weicools")
            .subText("China Beijing")
            .icon(R.drawable.ic_profile)
            .build())
        .addItem(ConvenienceBuilder.createWebsiteActionItem(
            c, ContextCompat.getDrawable(c, R.drawable.ic_brower),
            "Visit Website", true, Uri.parse("https://weicools.com")))
        .addItem(ConvenienceBuilder.createEmailItem(
            c, ContextCompat.getDrawable(c, R.drawable.ic_email),
            "Send an email", true, "lecymeng@outlook.com",
            "Question concerning MaterialAboutLibrary"))
        .addItem(ConvenienceBuilder.createPhoneItem(
            c, ContextCompat.getDrawable(c, R.drawable.ic_phone),
            "Call me", true, "+86 15185584134"))
        .addItem(new MaterialAboutActionItem.Builder()
            .text("Fork on GitHub")
            .icon(R.drawable.ic_github)
            .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/lecymeng")))
            .build());

    return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build());
  }


  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    setTheme(R.style.AppTheme_MaterialAboutActivity_Light);
    super.onCreate(savedInstanceState);
  }


  @Override
  protected CharSequence getActivityTitle() {
    return getString(R.string.mal_title_about);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu__about, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_refresh) {
      refreshMaterialAboutList();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
