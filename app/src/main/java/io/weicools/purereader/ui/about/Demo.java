package io.weicools.purereader.ui.about;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.OpenSourceLicense;
import io.weicools.purereader.R;
import io.weicools.purereader.ui.setting.LicenseActivity;

public class Demo {
  // public static MaterialAboutList createMaterialAboutList(final Context c, final int colorIcon, final int theme) {
  public static MaterialAboutList createMaterialAboutList(final Context c) {
    MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
    // Add items to card
    appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
        .text("Material About Library")
        .desc("© 2017 Daniel Stone")
        .icon(R.mipmap.ic_launcher)
        .build());

    appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Version",
        false));

    appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
        .text("Changelog")
        .icon(ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp))
        .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Releases", "https://github.com/daniel-stoneuk/material-about-library/releases", true, false))
        .build());

    appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
        .text("Licenses")
        .icon(R.drawable.ic_insert_link_24dp)
        .setOnClickAction(() -> {
          Intent intent = new Intent(c, LicenseActivity.class);
          // intent.putExtra(ExampleMaterialAboutActivity.THEME_EXTRA, theme);
          c.startActivity(intent);
        })
        .build());

    MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
    authorCardBuilder.title("Author");

    authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
        .text("Daniel Stone")
        .subText("United Kingdom")
        .icon(R.drawable.ic_insert_link_24dp)
        .build());

    authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
        .text("Fork on GitHub")
        .icon(R.drawable.ic_insert_link_24dp)
        .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/daniel-stoneuk")))
        .build());

    MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

    convenienceCardBuilder.title("Convenience Builder");

    convenienceCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Version",
        false));

    convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Visit Website",
        true,
        Uri.parse("http://daniel-stone.uk")));

    convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Rate this app",
        null
    ));

    convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Send an email",
        true,
        "apps@daniel-stone.uk",
        "Question concerning MaterialAboutLibrary"));

    convenienceCardBuilder.addItem(ConvenienceBuilder.createPhoneItem(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Call me",
        true,
        "+44 12 3456 7890"));

    convenienceCardBuilder.addItem(ConvenienceBuilder.createMapItem(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Visit London",
        null,
        "London Eye"));

    MaterialAboutCard.Builder otherCardBuilder = new MaterialAboutCard.Builder();
    otherCardBuilder.title("Other");

    otherCardBuilder.cardColor(Color.parseColor("#7986CB"));

    otherCardBuilder.addItem(new MaterialAboutActionItem.Builder()
        .icon(R.drawable.ic_insert_link_24dp)
        .text("HTML Formatted Sub Text")
        .subTextHtml("This is <b>HTML</b> formatted <i>text</i> <br /> This is very cool because it allows lines to get very long which can lead to all kinds of possibilities. <br /> And line breaks. <br /> Oh and by the way, this card has a custom defined background.")
        .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
        .build()
    );

    return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), convenienceCardBuilder.build(), otherCardBuilder.build());
  }


  public static MaterialAboutList createMaterialAboutLicenseList(final Context c, int colorIcon) {
    MaterialAboutCard materialAboutLIbraryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "material-about-library", "2016", "Daniel Stone",
        OpenSourceLicense.APACHE_2);

    MaterialAboutCard androidIconicsLicenseCard = ConvenienceBuilder.createLicenseCard(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "Android Iconics", "2016", "Mike Penz",
        OpenSourceLicense.APACHE_2);

    MaterialAboutCard leakCanaryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "LeakCanary", "2015", "Square, Inc",
        OpenSourceLicense.APACHE_2);

    MaterialAboutCard mitLicenseCard = ConvenienceBuilder.createLicenseCard(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "MIT Example", "2017", "Matthew Ian Thomson",
        OpenSourceLicense.MIT);

    MaterialAboutCard gplLicenseCard = ConvenienceBuilder.createLicenseCard(c,
        ContextCompat.getDrawable(c, R.drawable.ic_insert_link_24dp),
        "GPL Example", "2017", "George Perry Lindsay",
        OpenSourceLicense.GNU_GPL_3);

    return new MaterialAboutList(materialAboutLIbraryLicenseCard,
        androidIconicsLicenseCard,
        leakCanaryLicenseCard,
        mitLicenseCard,
        gplLicenseCard);
  }
}