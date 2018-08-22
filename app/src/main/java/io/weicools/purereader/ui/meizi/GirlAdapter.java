package io.weicools.purereader.ui.meizi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import io.weicools.purereader.R;
import io.weicools.purereader.data.GankContent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018/4/14.
 * <p>
 * desc:
 */
public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.GirlHolder> {
  private Context mContext;
  private List<GankContent> mDataList;

  public GirlAdapter (Context context) {
    mContext = context;
    mDataList = new ArrayList<>();
  }

  public void setDataList (List<GankContent> dataList) {
    mDataList = dataList;
    notifyDataSetChanged();
  }

  public void updateData (List<GankContent> dataList) {
    int position = mDataList.size();
    mDataList.addAll(dataList);
    notifyItemRangeChanged(position, dataList.size());
  }

  @NonNull
  @Override
  public GirlHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_girls, parent, false);
    return new GirlHolder(view);
  }

  @Override
  public void onBindViewHolder (@NonNull GirlHolder holder, int position) {
    final GankContent data = mDataList.get(position);
    RequestOptions options = new RequestOptions().placeholder(R.drawable.img_place_miku).error(R.drawable.img_load_error);
    Glide.with(mContext).applyDefaultRequestOptions(options).load(data.getUrl() + "?imageView2/0/w/250").into(holder.ivGirl);
    holder.itemView.setOnClickListener(
        v -> BigImageActivity.startBigImageActivity(mContext, data.getUrl(), data.getDesc()));
  }

  @Override
  public int getItemCount () {
    return mDataList.size();
  }

  static class GirlHolder extends RecyclerView.ViewHolder {
    final ImageView ivGirl;

    GirlHolder (View itemView) {
      super(itemView);
      ivGirl = itemView.findViewById(R.id.iv_girl);
    }
  }
}
