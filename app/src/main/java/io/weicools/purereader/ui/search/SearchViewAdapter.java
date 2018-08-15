package io.weicools.purereader.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.weicools.purereader.R;
import io.weicools.purereader.data.SearchResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by weicools on 2018.08.15
 *
 * desc:
 */
public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> {
  private Context mContext;
  private List<SearchResult> mResultList;


  SearchViewAdapter(Context context) {
    mContext = context;
    mResultList = new ArrayList<>();
  }


  public void updateResult(List<SearchResult> resultList) {
    mResultList = resultList;
    notifyDataSetChanged();
  }


  @NonNull @Override public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false);
    // View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_result, parent, false);
    return new SearchViewHolder(view);
  }


  @Override public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
    SearchResult result = mResultList.get(position);
    holder.tvWho.setText(result.getWho());
    holder.tvTitle.setText(result.getDesc());
    holder.tvType.setText("sss");
  }


  @Override public int getItemCount() {
    return mResultList.size();
  }


  static class SearchViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_who) TextView tvWho;
    @BindView(R.id.tv_time) TextView tvTime;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_type) TextView tvType;
    @BindView(R.id.iv_collect) ImageView ivCollect;


    SearchViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}