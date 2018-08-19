package io.weicools.purereader.ui.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.weicools.purereader.R;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018.08.19
 *
 * desc:
 */
public class GankHistoryAdapter extends RecyclerView.Adapter<GankHistoryAdapter.GankHistoryHolder> {
  private Context mContext;
  private List<String> mDateList;
  private OnSelectDateListener mListener;

  GankHistoryAdapter (Context context, OnSelectDateListener listener) {
    mContext = context;
    mListener = listener;
    mDateList = new ArrayList<>();
  }

  public void setDateList (List<String> results) {
    mDateList = results;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public GankHistoryHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_history, parent, false);
    return new GankHistoryHolder(view);
  }

  @Override
  public void onBindViewHolder (@NonNull GankHistoryHolder holder, int position) {
    String date = mDateList.get(position);
    holder.tvHistoryItem.setText(date);
    holder.tvHistoryItem.setOnClickListener(view -> mListener.onSelectDate(date));
  }

  @Override
  public int getItemCount () {
    return mDateList.size();
  }

  static class GankHistoryHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_history_item) TextView tvHistoryItem;

    GankHistoryHolder (View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public interface OnSelectDateListener {
    /**
     * select date
     *
     * @param date date
     */
    void onSelectDate (String date);
  }
}
