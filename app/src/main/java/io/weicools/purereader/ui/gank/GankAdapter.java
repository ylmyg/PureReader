package io.weicools.purereader.ui.gank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import io.weicools.purereader.R;
import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.ui.web.WebActivity;
import io.weicools.purereader.util.DateTimeUtil;
import io.weicools.purereader.util.ImageLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018/4/12.
 * <p>
 * desc:
 */
public class GankAdapter extends RecyclerView.Adapter<GankAdapter.GankHolder> {
  public static final int LIST_TYPE_DAILY = 0;
  public static final int LIST_TYPE_CATEGORY = 1;
  public static final int LIST_TYPE_FAVORITE = 2;

  private int mListType;
  private Context mContext;
  private List<GankContent> mDataList;

  public GankAdapter (Context context, int listType) {
    mContext = context;
    mListType = listType;
    mDataList = new ArrayList<>();
  }

  public void setDataList (List<GankContent> dataList) {
    mDataList = dataList;
    notifyDataSetChanged();
  }

  public void updateData (@NonNull List<GankContent> list) {
    int insertionPos = this.mDataList.size();
    this.mDataList.addAll(list);
    notifyItemRangeInserted(insertionPos, list.size());
  }

  @NonNull
  @Override
  public GankHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false);
    return new GankHolder(view);
  }

  @Override
  public void onBindViewHolder (@NonNull GankHolder holder, int position) {
    GankContent data = mDataList.get(position);

    holder.tvWho.setText(mContext.getString(R.string.text_who, data.getWho()));
    holder.tvTime.setText(DateTimeUtil.getTimeStr(data.getPublishedAt(), DateTimeUtil.DATE_FORMAT_STYLE4));
    holder.tvTitle.setText(data.getDesc());

    if (mListType == LIST_TYPE_CATEGORY) {
      holder.tvType.setVisibility(View.INVISIBLE);
    } else {
      holder.tvType.setText(mContext.getString(R.string.text_category, data.getType()));
    }

    List<String> imgList = data.getImages();
    if (imgList != null) {
      ImageLoader.getInstance().loadImage(holder.ivImage, imgList.get(0), R.drawable.ic_image_black_24dp);
    }

    holder.itemView.setOnClickListener(
        v -> WebActivity.startWebActivity(mContext, data, mListType == LIST_TYPE_FAVORITE));
  }

  @Override
  public int getItemCount () {
    return mDataList.size();
  }

  static class GankHolder extends RecyclerView.ViewHolder {
    final TextView tvWho;
    final TextView tvTime;
    final TextView tvTitle;
    final TextView tvType;
    final ImageView ivImage;

    GankHolder (View itemView) {
      super(itemView);
      tvWho = itemView.findViewById(R.id.tv_who);
      tvTime = itemView.findViewById(R.id.tv_time);
      tvTitle = itemView.findViewById(R.id.tv_title);
      tvType = itemView.findViewById(R.id.tv_type);
      ivImage = itemView.findViewById(R.id.iv_image);
    }
  }
}
