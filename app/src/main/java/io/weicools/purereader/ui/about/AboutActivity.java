package io.weicools.purereader.ui.about;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.ViewTypeManager;
import io.weicools.purereader.R;

public class AboutActivity extends MaterialAboutActivity {
  protected int colorIcon = R.color.mal_color_icon_light_theme;


  @NonNull
  @Override
  protected MaterialAboutList getMaterialAboutList(@NonNull final Context c) {
    MaterialAboutCard.Builder advancedCardBuilder = new MaterialAboutCard.Builder();
    advancedCardBuilder.title("Advanced");

    advancedCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
        .text("TitleItem OnClickAction")
        .icon(R.mipmap.ic_launcher)
        .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("http://www.daniel-stone.uk")))
        .build());

    advancedCardBuilder.addItem(new MaterialAboutActionItem.Builder()
        .text("Snackbar demo")
        .icon(R.drawable.ic_action_navigation_arrow_back)
        .setOnClickAction(() -> Snackbar.make(AboutActivity.this.findViewById(R.id.mal_material_about_activity_coordinator_layout), "Test", Snackbar.LENGTH_SHORT).show())
        .build());

    advancedCardBuilder.addItem(new MaterialAboutActionItem.Builder()
        .text("OnLongClickAction demo")
        .icon(R.drawable.ic_action_navigation_arrow_back)
        .setOnLongClickAction(() -> Toast.makeText(c, "Long pressed", Toast.LENGTH_SHORT).show())
        .build());

    advancedCardBuilder.addItem(new MyCustomItem.Builder()
        .text("Custom Item")
        .icon(R.drawable.ic_action_navigation_arrow_back)
        .build());

    advancedCardBuilder.addItem(createDynamicItem("Tap for a random number & swap position", c));

    return Demo.createMaterialAboutList(c);
  }


  private MaterialAboutActionItem createDynamicItem(String subText, final Context c) {
    final MaterialAboutActionItem item = new MaterialAboutActionItem.Builder()
        .text("Dynamic UI")
        .subText(subText)
        .icon(R.drawable.ic_action_navigation_arrow_back)
        .build();
    item.setOnClickAction(() -> {
      getList().getCards().get(4).getItems().remove(getList().getCards().get(4).getItems().indexOf(item));
      int newIndex = ((int) (Math.random() * 5));
      getList().getCards().get(4).getItems().add(newIndex, item);
      item.setSubText("Random number: " + ((int) (Math.random() * 10)));
      setMaterialAboutList(getList());
    });

    return item;
  }


  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    setTheme(R.style.AppTheme_MaterialAboutActivity_Light);
    // colorIcon = R.color.mal_color_icon_light_theme;
    super.onCreate(savedInstanceState);
  }


  @Override
  protected CharSequence getActivityTitle() {
    return getString(R.string.mal_title_about);
  }


  @NonNull
  @Override
  protected ViewTypeManager getViewTypeManager() {
    return new MyViewTypeManager();
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
