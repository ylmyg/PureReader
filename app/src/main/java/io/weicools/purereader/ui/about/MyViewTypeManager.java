package io.weicools.purereader.ui.about;

import android.content.Context;
import android.view.View;
import com.danielstone.materialaboutlibrary.holders.MaterialAboutItemViewHolder;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.util.ViewTypeManager;
import io.weicools.purereader.R;

public class MyViewTypeManager extends ViewTypeManager {

  public static final class ItemType {
    public static final int ACTION_ITEM = ViewTypeManager.ItemType.ACTION_ITEM;
    public static final int TITLE_ITEM = ViewTypeManager.ItemType.TITLE_ITEM;
    public static final int CUSTOM_ITEM = 10;
  }


  public static final class ItemLayout {
    public static final int ACTION_LAYOUT = ViewTypeManager.ItemLayout.ACTION_LAYOUT;
    public static final int TITLE_LAYOUT = ViewTypeManager.ItemLayout.TITLE_LAYOUT;
    public static final int CUSTOM_LAYOUT = R.layout.custom_item;
  }


  @Override public int getLayout(int itemType) {
    switch (itemType) {
      case ItemType.ACTION_ITEM:
        return ItemLayout.ACTION_LAYOUT;
      case ItemType.TITLE_ITEM:
        return ItemLayout.TITLE_LAYOUT;
      case ItemType.CUSTOM_ITEM:
        return ItemLayout.CUSTOM_LAYOUT;
      default:
        return -1;
    }
  }


  @Override public MaterialAboutItemViewHolder getViewHolder(int itemType, View view) {
    switch (itemType) {
      case ItemType.ACTION_ITEM:
        return MaterialAboutActionItem.getViewHolder(view);
      case ItemType.TITLE_ITEM:
        return MaterialAboutTitleItem.getViewHolder(view);
      case ItemType.CUSTOM_ITEM:
        return MyCustomItem.getViewHolder(view);
      default:
        return null;
    }
  }


  @Override public void setupItem(int itemType, MaterialAboutItemViewHolder holder, MaterialAboutItem item, Context context) {
    switch (itemType) {
      case ItemType.ACTION_ITEM:
        MaterialAboutActionItem.setupItem((MaterialAboutActionItem.MaterialAboutActionItemViewHolder) holder, (MaterialAboutActionItem) item, context);
        break;
      case ItemType.TITLE_ITEM:
        MaterialAboutTitleItem.setupItem((MaterialAboutTitleItem.MaterialAboutTitleItemViewHolder) holder, (MaterialAboutTitleItem) item, context);
        break;
      case ItemType.CUSTOM_ITEM:
        MyCustomItem.setupItem((MyCustomItem.MyCustomItemViewHolder) holder, (MyCustomItem) item, context);
        break;
      default:
        break;
    }
  }
}
