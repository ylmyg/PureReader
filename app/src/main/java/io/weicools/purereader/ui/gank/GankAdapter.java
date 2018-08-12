package io.weicools.purereader.ui.gank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.util.DateFormatUtil;
import java.util.ArrayList;
import java.util.List;

import io.weicools.purereader.R;
import io.weicools.purereader.ui.web.WebActivity;

/**
 * @author Weicools Create on 2018/4/12.
 * <p>
 * desc:
 */
public class GankAdapter extends RecyclerView.Adapter<GankAdapter.GankHolder> {
  private Context mContext;
  private List<GankContent> mDataList;


  public GankAdapter(Context context) {
    mContext = context;
    mDataList = new ArrayList<>();
  }


  public void setDataList(List<GankContent> dataList) {
    mDataList = dataList;
    notifyDataSetChanged();
  }


  public void updateData(@NonNull List<GankContent> list) {
    int insertionPos = this.mDataList.size();
    this.mDataList.addAll(list);
    notifyItemRangeInserted(insertionPos, list.size());
    //        mDataList.clear();
    //        mDataList.addAll(list);
    //        notifyDataSetChanged();
    //        notifyItemRemoved(list.size());
  }


  @NonNull
  @Override
  public GankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false);
    return new GankHolder(view);
  }


  @Override
  public void onBindViewHolder(@NonNull GankHolder holder, int position) {
    final GankContent data = mDataList.get(position);

    holder.tvWho.setText(data.getWho());
    holder.tvTime.setText(DateFormatUtil.getTimeStr(data.getCreatedAt()));
    holder.tvTitle.setText(data.getDesc());
    holder.tvType.setText(data.getType());
    holder.itemView.setOnClickListener(v -> {
      String imgUrl = data.getImages().get(0);
      WebActivity.startWebActivity(mContext, data.getUrl(), data.getDesc(), imgUrl);
    });
  }


  @Override
  public int getItemCount() {
    return mDataList.size();
  }


  static class GankHolder extends RecyclerView.ViewHolder {
    final TextView tvWho;
    final TextView tvTime;
    final TextView tvTitle;
    final TextView tvType;
    final ImageView ivCollect;


    GankHolder(View itemView) {
      super(itemView);
      tvWho = itemView.findViewById(R.id.tv_who);
      tvTime = itemView.findViewById(R.id.tv_time);
      tvTitle = itemView.findViewById(R.id.tv_title);
      tvType = itemView.findViewById(R.id.tv_type);
      ivCollect = itemView.findViewById(R.id.iv_collect);
    }
  }
}
