package io.weicools.purereader.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Immutable model class for a zhihu daily news post.
 * Entity class for {@link com.google.gson.Gson}.
 */

public class ZhihuDailyNews {
    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("stories")
    private List<ZhihuDailyNewsQuestion> stories;

    public String getDate() {
        return date;
    }

    public List<ZhihuDailyNewsQuestion> getStories() {
        return stories;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<ZhihuDailyNewsQuestion> stories) {
        this.stories = stories;
    }
}
