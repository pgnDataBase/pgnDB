package com.engwork.pgndb.chessgames;

import com.engwork.pgndb.chessgames.ChessGamesRequest.ChessGamesMetadataRequest;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ChessGamesMapper {

  String selectChessGames = "select * from games order by id limit #{pageSize} offset #{offset}";
  String selectChessGamesIds = "select id from games";
  String selectChessGameById = "select * from games where id=#{id}";
  String selectChessGamesNumber = "select count(*) from games";
  String insertChessGameToDatabase = "insert into games"
      + "(id,white_player_id,black_player_id,event_id,result,description,additional_info,game_round,game_date) values "
      + "(#{id},#{whitePlayer},#{blackPlayer},#{eventId},#{result},#{description},#{additionalInfo},#{round},#{date})";
  String deleteChessGameById = "delete from games where id=#{id}";
  String updateChessGame = "update games set white_player_id=#{whitePlayer},black_player_id=#{blackPlayer}," +
      "event_id=#{eventId},result=#{result},description=#{description},additional_info=#{additionalInfo},game_round=#{round},game_date=#{date} where id=#{id}";


  @Select("select id from games order by id limit #{limit} offset #{offset}")
  List<UUID> selectChessGamesIdsLimitOffset(@Param("limit")Integer limit, @Param("offset")Integer offset);

  @Select(selectChessGamesIds)
  List<UUID> selectChessGamesIds();

  @Select(selectChessGames)
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGames(ChessGamesMetadataRequest chessGamesMetadataRequest);

  @Select(selectChessGameById)
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  ChessGameMetadataDAO selectChessGameById(UUID id);

  @Select(selectChessGamesNumber)
  Integer selectChessGamesNumber();

  @Insert(insertChessGameToDatabase)
  void insertChessGameToDatabase(ChessGameMetadataDAO chessGameMetadataDAO);

  @Delete(deleteChessGameById)
  void deleteChessGameById(UUID id);

  @Update(updateChessGame)
  void updateChessGame(ChessGameMetadataDAO chessGameMetadataDAO);

  @Insert({
      "<script>",
      "INSERT INTO games",
      "(id,white_player_id,black_player_id,event_id,result,description,additional_info,game_round,game_date)",
      " VALUES <foreach item='item_name' collection='games' open='' separator=',' close=''>",
      "(#{item_name.id},",
      "#{item_name.whitePlayer},",
      "#{item_name.blackPlayer},",
      "#{item_name.eventId},",
      "#{item_name.result},",
      "#{item_name.description},",
      "#{item_name.additionalInfo},",
      "#{item_name.round},",
      "#{item_name.date})",
      "</foreach>",
      "</script>"
  })
  void insertMany(@Param("games") List<ChessGameMetadataDAO> games);

  // SEARCHING BY HEADERS
  @Select({
      "<script>",
      "SELECT * ",
      "FROM games ",
      "WHERE event_id ",
      "IN <foreach item='item_name' collection='events_ids' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>",
      "</script>"
  })
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGamesByManyEventIds(@Param("events_ids") List<UUID> eventsIds);

  @Select({
      "<script>",
      "SELECT * ",
      "FROM games ",
      "WHERE white_player_id ",
      "IN <foreach item='item_name' collection='ids' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>",
      "</script>"
  })
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGamesByWhitePlayerIds(@Param("ids") List<UUID> ids);


  @Select({
      "<script>",
      "SELECT * ",
      "FROM games ",
      "WHERE black_player_id ",
      "IN <foreach item='item_name' collection='ids' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>",
      "</script>"
  })
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGamesByBlackPlayerIds(@Param("ids") List<UUID> ids);

  @Select("SELECT * FROM games WHERE LOWER(result) LIKE '%' || LOWER(#{result}) || '%'")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGamesByResult(@Param("result") String result);

  @Select("SELECT * FROM games where LOWER(game_round) LIKE '%' || LOWER(#{round}) || '%'")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGamesByRound(@Param("round") String round);

  @Select("SELECT * FROM games where LOWER(game_date) LIKE '%' || LOWER(#{date}) || '%'")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGamesByDate(@Param("date") String date);

  @Select("SELECT * FROM games where LOWER(additional_info) LIKE '%' || LOWER(#{tag}) || '%'")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "whitePlayer", column = "white_player_id"),
          @Result(property = "blackPlayer", column = "black_player_id"),
          @Result(property = "eventId", column = "event_id"),
          @Result(property = "result", column = "result"),
          @Result(property = "description", column = "description"),
          @Result(property = "additionalInfo", column = "additional_info"),
          @Result(property = "round", column = "game_round"),
          @Result(property = "date", column = "game_date")
      }
  )
  List<ChessGameMetadataDAO> selectChessGamesByAdditionalTag(@Param("tag") String tag);
}
