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
    private List<T> results = null;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
