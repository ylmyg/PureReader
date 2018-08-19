package io.weicools.purereader.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Weicools Create on 2017/12/2.
 * <p>
 * A util class for formatting the date between string and long.
 */
public final class DateTimeUtil {
  private static final String DATE_FORMAT_STYLE1 = "yyyy-MM-dd";
  private static final String DATE_FORMAT_STYLE2 = "yyyy/MM/dd";
  private static final String DATE_FORMAT_STYLE3 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final int TIME_INTERVAL = 1000;

  private DateTimeUtil () {
    throw new AssertionError("No construction for constant class");
  }

  public static long getCurrTime () {
    return System.currentTimeMillis() / TIME_INTERVAL;
  }

  public static String getTimeStr (String time) {
    DateFormat df = new SimpleDateFormat(DATE_FORMAT_STYLE3, Locale.getDefault());
    long diff;
    try {
      Date publishDate = df.parse(time);
      Date nowDate = new Date();
      diff = nowDate.getTime() - publishDate.getTime();
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
          if (diff == 1) {
            return "昨天";
          }
          return diff + "天前";
        }
        DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dfDate.format(publishDate);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return time;
  }

  public static String dateFormat (String dateStr) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    Date date = new Date();
    try {
      date = simpleDateFormat.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_STYLE2, Locale.US);
    return simpleDateFormat.format(date);
  }

  public static String formatZhihuDailyDateLongToString (long date) {
    String sDate;
    Date d = new Date(date + 24 * 60 * 60 * 1000);
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    sDate = format.format(d);
    return sDate;
  }

  public static long formatZhihuDailyDateStringToLong (String date) {
    Date d = null;
    try {
      d = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return d == null ? 0 : d.getTime();
  }

  public static String formatDoubanMomentDateLongToString (long date) {
    String sDate;
    Date d = new Date(date);
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STYLE1, Locale.getDefault());
    sDate = format.format(d);

    return sDate;
  }

  public static Date formatStringToDate (String date) {
    Date d = null;
    try {
      d = new SimpleDateFormat(DATE_FORMAT_STYLE1, Locale.getDefault()).parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return d == null ? new Date() : d;
  }

  public static int getYearFromDate (Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.YEAR);
  }

  public static int getMonthFromDate (Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.MONTH);
  }

  public static int getDayFromDate (Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  public static long formatGuokrHandpickTimeStringToLong (String date) {
    Date d = null;
    try {
      d = new SimpleDateFormat(DATE_FORMAT_STYLE1, Locale.getDefault()).parse(date.substring(0, 10));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return d == null ? 0 : d.getTime();
  }
}
