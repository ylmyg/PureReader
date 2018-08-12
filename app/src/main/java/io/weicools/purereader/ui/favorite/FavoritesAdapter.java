package io.weicools.purereader.ui.favorite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import io.weicools.purereader.data.GankContent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018/4/15.
 * <p>
 * desc:
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {
  private Context mContext;
  private List<GankContent> mDataList;


  public FavoritesAdapter(Context context) {
    mContext = context;
    mDataList = new ArrayList<>();
  }


  public void setDataList(List<GankContent> dataList) {
    mDataList = dataList;
    notifyDataSetChanged();
  }


  @NonNull
  @Override
  public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }


  @Override
  public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {

  }


  @Override
  public int getItemCount() {
    return mDataList.size();
  }


  static class FavoritesHolder extends RecyclerView.ViewHolder {

    FavoritesHolder(View itemView) {
      super(itemView);
    }
  }
}
