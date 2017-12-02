package io.weicools.purereader.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Content types of in annotation type.
 */

@Retention(SOURCE)
@IntDef({PostType.TYPE_ZHIHU})
public @interface PostType {
    int TYPE_ZHIHU = 0;
}
