package io.weicools.purereader.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * @author Weicools Create on 2018/4/15.
 * <p>
 * desc:
 */
public abstract class LoadMoreRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
  // The total number of items in the dataset after the last load
  private int previousTotal = 0;
  private boolean isLoading;
  //list到达 最后一个item的时候 触发加载
  private int visibleThreshold = 1;
  // The minimum amount of items to have below your current scroll position before loading more.
  private int firstVisibleItem, visibleItemCount, totalItemCount;
  //默认第一页
  private int currentPage = 1;

  private RecyclerView.LayoutManager mLayoutManager;

  public LoadMoreRecyclerOnScrollListener (RecyclerView.LayoutManager layoutManager) {
    this.mLayoutManager = layoutManager;
  }

  @Override
  public void onScrolled (RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    visibleItemCount = recyclerView.getChildCount();
    if (mLayoutManager instanceof LinearLayoutManager) {
      totalItemCount = mLayoutManager.getItemCount();
      firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
    }
    if (mLayoutManager instanceof StaggeredGridLayoutManager) {
      totalItemCount = mLayoutManager.getItemCount();
      int[] lastPositions = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(
          new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()]);
      firstVisibleItem = getMinPositions(lastPositions);
    }

    //判断加载完成了...
    if (isLoading) {
      if (totalItemCount > previousTotal) {
        isLoading = false;
        previousTotal = totalItemCount;
      }
    }
    if (!isLoading && totalItemCount > visibleItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem
        + visibleThreshold)) {
      currentPage++;
      onLoadMore(currentPage);
      isLoading = true;
      //loadMore(currentPage);
    }
  }

  /**
   * 获得当前展示最小的position
   */
  private int getMinPositions (int[] positions) {
    int size = positions.length;
    int minPosition = Integer.MAX_VALUE;
    for (int i = 0; i < size; i++) {
      minPosition = Math.min(minPosition, positions[i]);
    }
    return minPosition;
  }

  public abstract void onLoadMore (int currentPage);
}
