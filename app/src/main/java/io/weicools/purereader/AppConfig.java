package io.weicools.purereader;

/**
 * Create by weicools on 2018/4/13.
 * <p>
 * desc:
 */

public final class AppConfig {
    private AppConfig() {
        throw new AssertionError("No construction for AppConfig class");
    }

    public static final String TYPE_DAILY = "今日干货";
    public static final String TYPE_ANDROID = "Android";
    public static final String TYPE_IOS = "iOS";
    public static final String TYPE_WEB_FONT = "前端";
    public static final String TYPE_APP = "App";
    public static final String TYPE_RECOMMEND = "拓展资源";
    public static final String TYPE_GIRLS = "福利";

    public static final String KEY_NO_IMG_MODE = "no_picture_mode";
    public static final String KEY_CHROME_CUSTOM_TABS = "chrome_custom_tabs";
    public static final String KEY_NIGHT_MODE = "night_mode";
    public static final String KEY_TIME_OF_SAVING_ARTICLES = "time_of_saving_articles";
}
