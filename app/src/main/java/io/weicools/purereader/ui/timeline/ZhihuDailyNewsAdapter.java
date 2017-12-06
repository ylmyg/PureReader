package io.weicools.purereader.ui.timeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import io.weicools.purereader.R;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.interfaze.OnRecyclerViewItemOnClickListener;

/**
 * Create by weicools on 2017/12/3.
 * <p>
 * Adapter between the data of {@link ZhihuDailyNewsQuestion} and {@link RecyclerView}.
 */

public class ZhihuDailyNewsAdapter extends RecyclerView.Adapter<ZhihuDailyNewsAdapter.ViewHolder> {
    @NonNull
    private final Context mContext;

    @NonNull
    private List<ZhihuDailyNewsQuestion> mList;
    private OnRecyclerViewItemOnClickListener mListener;

    public ZhihuDailyNewsAdapter(@NonNull Context context,
                                 @NonNull List<ZhihuDailyNewsQuestion> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_universal_layout, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ZhihuDailyNewsQuestion item = mList.get(position);

        if (item.getImages().get(0) == null) {
            holder.itemImg.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext)
                    .load(item.getImages().get(0))
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(holder.itemImg);
        }
        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mList.isEmpty() ? 0 : mList.size();
    }

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener) {
        this.mListener = listener;
    }

    public void updateData(@NonNull List<ZhihuDailyNewsQuestion> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView itemImg;
        private TextView title;
        private OnRecyclerViewItemOnClickListener listener;

        ViewHolder(View itemView, OnRecyclerViewItemOnClickListener listener) {
            super(itemView);

            itemImg = itemView.findViewById(R.id.image_view_cover);
            title = itemView.findViewById(R.id.text_view_title);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }
}
