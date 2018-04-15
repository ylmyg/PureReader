package io.weicools.purereader.ui.timeline;

import android.content.Context;
import android.content.Intent;
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
import io.weicools.purereader.data.ContentType;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.ui.detail.DetailsActivity;

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

    public ZhihuDailyNewsAdapter(@NonNull Context context,
                                 @NonNull List<ZhihuDailyNewsQuestion> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_universal_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ZhihuDailyNewsQuestion item = mList.get(position);

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(DetailsActivity.KEY_ARTICLE_ID, item.getId());
                intent.putExtra(DetailsActivity.KEY_ARTICLE_TYPE, ContentType.TYPE_ZHIHU_DAILY);
                intent.putExtra(DetailsActivity.KEY_ARTICLE_TITLE, item.getTitle());
                intent.putExtra(DetailsActivity.KEY_ARTICLE_IS_FAVORITE, item.isFavorite());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.isEmpty() ? 0 : mList.size();
    }

    public void updateData(@NonNull List<ZhihuDailyNewsQuestion> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImg;
        private TextView title;

        ViewHolder(View itemView) {
            super(itemView);

            itemImg = itemView.findViewById(R.id.image_view_cover);
            title = itemView.findViewById(R.id.text_view_title);
        }
    }
}
