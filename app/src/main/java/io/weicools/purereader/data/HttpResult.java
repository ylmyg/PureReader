package io.weicools.purereader.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by weicools on 2018/4/12.
 * <p>
 * desc:
 */

public class HttpResult<T> {
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("results")
    @Expose
    private T results;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
