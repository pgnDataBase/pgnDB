package com.engwork.pgndb.chessgamesloader.mappers;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MovesMapper {
  String insertMove = "insert into " +
      "moves(id,move_number,start_field,final_field,game_id,move_type,moved_piece_code,captured_piece_code,promoted_piece_code,variant_id,comment,san,entity_number,fen,variant_type)" +
      "values(#{id},#{moveNumber},#{fromField},#{toField},#{gameId}," +
      "#{moveType},#{pieceCode},#{capturedPieceCode},#{promotedPieceCode},#{variantId},#{comment},#{san},#{entityNumber},#{fen},#{variantType})";

  String selectMovesByGameId = "select * from moves where game_id=#{id} order by entity_number";
  String deleteMovesByGameId = "delete from moves where game_id=#{gameId}";
  String selectMovesCountByGameId = "select count(*) from moves where game_id=#{id}";


  @Select(selectMovesCountByGameId)
  Integer selectMovesCountByGameId(UUID id);


  @Select(selectMovesByGameId)
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "moveNumber", column = "move_number"),
          @Result(property = "fromField", column = "start_field"),
          @Result(property = "toField", column = "final_field"),
          @Result(property = "gameId", column = "game_id"),
          @Result(property = "moveType", column = "move_type"),
          @Result(property = "pieceCode", column = "moved_piece_code"),
          @Result(property = "promotedPieceCode", column = "promoted_piece_code"),
          @Result(property = "capturedPieceCode", column = "captured_piece_code"),
          @Result(property = "comment", column = "comment"),
          @Result(property = "variantId", column = "variant_id"),
          @Result(property = "san", column = "san"),
          @Result(property = "entityNumber", column = "entity_number"),
          @Result(property = "fen", column = "fen"),
          @Result(property = "positionId", column = "position_id"),
          @Result(property = "variantType", column = "variant_type")
      }
  )
  List<Move> selectMovesByGameId(UUID id);

  @Insert(insertMove)
  void insertMove(Move move);

  @Insert({
      "<script>",
      "INSERT INTO moves",
      "(ID,GAME_ID,MOVE_NUMBER,START_FIELD,FINAL_FIELD,MOVE_TYPE,MOVED_PIECE_CODE,PROMOTED_PIECE_CODE,CAPTURED_PIECE_CODE,COMMENT,VARIANT_ID,FEN,POSITION_ID,VARIANT_TYPE,ENTITY_NUMBER,SAN)",
      " VALUES <foreach item='item_name' collection='moves' open='' separator=',' close=''>",
      "(#{item_name.id},",
      "#{item_name.gameId},",
      "#{item_name.moveNumber},",
      "#{item_name.fromField},",
      "#{item_name.toField},",
      "#{item_name.moveType},",
      "#{item_name.pieceCode},",
      "#{item_name.promotedPieceCode},",
      "#{item_name.capturedPieceCode},",
      "#{item_name.comment},",
      "#{item_name.variantId},",
      "#{item_name.fen},",
      "#{item_name.positionId},",
      "#{item_name.variantType},",
      "#{item_name.entityNumber},",
      "#{item_name.san})",
      "</foreach>",
      "</script>"
  })
  void insertMany(@Param("moves") List<Move> moves);

  @Delete(deleteMovesByGameId)
  void deleteMovesByGameId(UUID gameId);

  //  SEARCHING
  @Select({
      "<script>",
      "SELECT * ",
      "FROM moves ",
      "WHERE position_id ",
      "IN <foreach item='item_name' collection='ids' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>",
      "</script>"
  })
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "moveNumber", column = "move_number"),
          @Result(property = "fromField", column = "start_field"),
          @Result(property = "toField", column = "final_field"),
          @Result(property = "gameId", column = "game_id"),
          @Result(property = "moveType", column = "move_type"),
          @Result(property = "pieceCode", column = "moved_piece_code"),
          @Result(property = "promotedPieceCode", column = "promoted_piece_code"),
          @Result(property = "capturedPieceCode", column = "captured_piece_code"),
          @Result(property = "comment", column = "comment"),
          @Result(property = "variantId", column = "variant_id"),
          @Result(property = "san", column = "san"),
          @Result(property = "entityNumber", column = "entity_number"),
          @Result(property = "fen", column = "fen"),
          @Result(property = "positionId", column = "position_id"),
          @Result(property = "variantType", column = "variant_type")
      }
  )
  List<Move> selectByPositionIds(@Param("ids") List<UUID> ids);

  @Select({
      "<script>",
      "SELECT * ",
      "FROM moves ",
      "WHERE position_id ",
      "IN <foreach item='item_name' collection='ids' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>" ,
      "AND variant_id IS NULL",
      "</script>"
  })
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "moveNumber", column = "move_number"),
          @Result(property = "fromField", column = "start_field"),
          @Result(property = "toField", column = "final_field"),
          @Result(property = "gameId", column = "game_id"),
          @Result(property = "moveType", column = "move_type"),
          @Result(property = "pieceCode", column = "moved_piece_code"),
          @Result(property = "promotedPieceCode", column = "promoted_piece_code"),
          @Result(property = "capturedPieceCode", column = "captured_piece_code"),
          @Result(property = "comment", column = "comment"),
          @Result(property = "variantId", column = "variant_id"),
          @Result(property = "san", column = "san"),
          @Result(property = "entityNumber", column = "entity_number"),
          @Result(property = "fen", column = "fen"),
          @Result(property = "positionId", column = "position_id"),
          @Result(property = "variantType", column = "variant_type")
      }
  )
  List<Move> selectByPositionIdsExcludeVariants(@Param("ids") List<UUID> ids);

  // TREE BUILDING
  @Select({
      "<script>",
      "SELECT * ",
      "FROM moves ",
      "WHERE game_id ",
      "IN <foreach item='item_name' collection='ids' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>",
      "order by game_id, entity_number",
      "</script>"
  })
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "moveNumber", column = "move_number"),
          @Result(property = "fromField", column = "start_field"),
          @Result(property = "toField", column = "final_field"),
          @Result(property = "gameId", column = "game_id"),
          @Result(property = "moveType", column = "move_type"),
          @Result(property = "pieceCode", column = "moved_piece_code"),
          @Result(property = "promotedPieceCode", column = "promoted_piece_code"),
          @Result(property = "capturedPieceCode", column = "captured_piece_code"),
          @Result(property = "comment", column = "comment"),
          @Result(property = "variantId", column = "variant_id"),
          @Result(property = "san", column = "san"),
          @Result(property = "entityNumber", column = "entity_number"),
          @Result(property = "fen", column = "fen"),
          @Result(property = "positionId", column = "position_id"),
          @Result(property = "variantType", column = "variant_type")
      }
  )
  List<Move> selectByGameIds(@Param("ids") List<UUID> gameIds);
}
