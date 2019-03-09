package com.engwork.pgndb.chessengine.utils;

import com.engwork.pgndb.chessengine.engineinfo.EngineInfo;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EngineInfoParser {
  private final static Pattern DEPTH_PATTERN = Pattern.compile("^.* depth ([0-9]*).*$");
  private final static Pattern SCORE_CP_PATTERN = Pattern.compile("^.*score cp ([-0-9]*).*$");
  private final static Pattern TIME_PATTERN=Pattern.compile("^.*time ([0-9]*).*$");

  private static String getPatternGroup(Pattern pattern,String info){
    Matcher matcher = pattern.matcher(info);
    if(matcher.matches() && matcher.groupCount()>0)
      return matcher.group(1);
    return "";
  }

  public static EngineInfo parse(String info, String fen){
    try {
      int depth = Integer.parseInt(getPatternGroup(DEPTH_PATTERN, info));
      Double scoreCentiPawn = Double.parseDouble(getPatternGroup(SCORE_CP_PATTERN, info)) / 100.0;
      long time = Long.parseLong(getPatternGroup(TIME_PATTERN, info));
      int movesStart = info.indexOf(" pv ") + 4;
      String principalVariation = info.substring(movesStart);
      String movesChain = PrincipalVariationParser.parse(principalVariation, fen);
      return new EngineInfo(fen, depth, scoreCentiPawn, milliscondsToHoursMinutesSeconds(time), movesChain);
    } catch (Exception e) {
      return null;
    }
  }

  private static String milliscondsToHoursMinutesSeconds(long duration) {
    long hours = TimeUnit.MILLISECONDS.toHours(duration);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
    return formatTime(hours)+":"+formatTime(minutes)+":"+formatTime(seconds);
  }

  private static String formatTime(long time){
    if(time < 10)
      return "0"+time;
    else
      return String.valueOf(time);
  }
}
