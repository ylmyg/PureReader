package io.weicools.purereader.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.weicools.purereader.R;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  weicools create on 2018.08.16
 *
 * desc:
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int TYPE_NORMAL = 0x01;
  private static final int TYPE_FOOT = 0x02;
  private static final int MAX_COUNT = 15;

  private int mDataCount;
  private Context mContext;
  private List<String> mData;
  private OnClickSearchListener mListener;

  public SearchHistoryAdapter (Context context, OnClickSearchListener listener) {
    mContext = context;
    mListener = listener;
    mData = new ArrayList<>();
  }

  public void updateSearchHistory (List<String> searchList) {
    searchList.add("Clear");
    mData = searchList;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
    if (viewType == TYPE_NORMAL) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_history, parent, false);
      return new HistoryViewHolder(view);
    }
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_clear, parent, false);
    return new ClearViewHolder(view);
  }

  @Override public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, int position) {
    if (position == mDataCount - 1) {
      ((ClearViewHolder) holder).itemView.setOnClickListener(v -> mListener.onClickClearHistory());
    } else {
      HistoryViewHolder viewHolder = (HistoryViewHolder) holder;
      String history = mData.get(position);
      viewHolder.tvHisContent.setText(history);
      viewHolder.itemView.setOnClickListener(v -> mListener.onClickHistoryItem(history));
    }
  }

  @Override public int getItemCount () {
    if (mData.size() < MAX_COUNT) {
      mDataCount = mData.size();
      return mDataCount;
    }
    mDataCount = MAX_COUNT;
    return mDataCount;
  }

  @Override public int getItemViewType (int position) {
    if (position == mDataCount - 1) {
      return TYPE_FOOT;
    }
    return TYPE_NORMAL;
  }

  static class HistoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvHisContent;

    HistoryViewHolder (final View itemView) {
      super(itemView);
      tvHisContent = itemView.findViewById(R.id.tv_history_item);
    }
  }

  static class ClearViewHolder extends RecyclerView.ViewHolder {
    ClearViewHolder (final View itemView) {
      super(itemView);
    }
  }

  public interface OnClickSearchListener {
    /**
     * click history
     * @param s content
     */
    void onClickHistoryItem (String s);

    /**
     * clear search history
     */
    void onClickClearHistory ();
  }
}
