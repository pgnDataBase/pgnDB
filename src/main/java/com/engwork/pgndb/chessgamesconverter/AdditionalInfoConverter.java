package com.engwork.pgndb.chessgamesconverter;

import java.util.HashMap;
import java.util.Map;

public class AdditionalInfoConverter {

  //Key and value separator
  public static final String K_V_SEP ="-->>";
  private static final String TAG_SEP =";;";

  public static String fromMapToString(Map<String,String> map){
    StringBuilder parsedMap= new StringBuilder();
    for (String key : map.keySet()){
      parsedMap.append(key+ K_V_SEP +map.get(key)+ TAG_SEP);
    }
    if(parsedMap.length()==0)
      return null;
    else
      return parsedMap.toString();
  }

  public static Map<String,String> fromStringToMap(String parsedMap){
    Map<String,String> map = new HashMap<>();
    if(parsedMap!=null) {
      for (String tag : parsedMap.split(TAG_SEP)) {
        if (tag.contains(K_V_SEP)) {
          String[] tagKeyAndValue = tag.split(K_V_SEP);
          if (tagKeyAndValue.length == 2)
            map.put(tagKeyAndValue[0], tagKeyAndValue[1]);
          else
            map.put(tagKeyAndValue[0], "");
        }
      }
    }
    return map;
  }
}
