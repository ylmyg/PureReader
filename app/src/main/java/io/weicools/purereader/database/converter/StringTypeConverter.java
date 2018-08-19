package io.weicools.purereader.database.converter;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weicools Create on 2018.08.17
 *
 * desc:
 */
public class StringTypeConverter {
  @TypeConverter
  public static String stringList2String (List<String> stringList) {
    return new Gson().toJson(stringList);
  }

  @TypeConverter
  public static List<String> string2StringList (String s) {
    return new Gson().fromJson(s, new TypeToken<ArrayList<String>>() {}.getType());
  }
}
