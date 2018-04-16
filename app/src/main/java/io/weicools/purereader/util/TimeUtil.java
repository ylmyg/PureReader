package io.weicools.purereader.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Create by weicools on 2018/4/15.
 * <p>
 * desc:
 */

public class TimeUtil {
    public static String getTimeStr(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        long diff = 0;
        try {
            Date publish_date = df.parse(time);
            Date now_date = new Date();
            diff = now_date.getTime() - publish_date.getTime();
            if (diff < 0) {
                return "from未来";
            } else {
                diff /= (1000 * 60);
                if (diff <= 60) {
                    return diff + "分钟前";
                }
                diff /= 60;
                if (diff <= 24) {
                    return diff + "小时前";
                }
                diff /= 24;
                if (diff < 30) {
                    if (diff == 1) return "昨天";
                    return diff + "天前";
                }
                DateFormat df_date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                return df_date.format(publish_date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String dateFormat(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        return simpleDateFormat.format(date);
    }
}
