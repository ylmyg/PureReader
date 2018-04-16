package io.weicools.purereader.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by weicools on 2018/4/15.
 * <p>
 * desc: 每日干货Data
 */

public class DailyGankData {
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("category")
    @Expose
    private List<String> category;

    @SerializedName("results")
    @Expose
    public Results results;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public static class Results {
        @SerializedName("Android")
        @Expose
        public List<GankData> androidList;

        @SerializedName("iOS")
        @Expose
        public List<GankData> iOSList;

        @SerializedName("前端")
        @Expose
        public List<GankData> webFontList;

        @SerializedName("App")
        @Expose
        public List<GankData> appList;

        @SerializedName("拓展资源")
        @Expose
        public List<GankData> resourceList;

        @SerializedName("瞎推荐")
        @Expose
        public List<GankData> recommendList;

        @SerializedName("休息视频")
        @Expose
        public List<GankData> videoList;

        @SerializedName("福利")
        @Expose
        public List<GankData> girlList;
    }
}
