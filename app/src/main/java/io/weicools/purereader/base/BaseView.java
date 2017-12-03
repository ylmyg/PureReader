package io.weicools.purereader.base;

import android.view.View;

/**
 * Create by weicools on 2017/12/3.
 * <p>
 * The base of all view layers.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);

    void initView(View view);
}
