package io.weicools.purereader.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by weicools on 2018/4/12.
 * <p>
 * desc:
 */

/*
            "_id": "5a96219b421aa9106fb5f460",
            "createdAt": "2018-02-28T11:27:23.566Z",
            "desc": "一个帮你轻松完成Api接口调试的IDEA插件",
            "publishedAt": "2018-03-12T08:44:50.326Z",
            "source": "web",
            "type": "拓展资源",
            "url": "https://github.com/fingerart/ApiDebugger",
            "used": true,
            "who": "指尖上的艺术"
 */

public class GankData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("used")
    @Expose
    private boolean used;
    @SerializedName("who")
    @Expose
    private String who;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
