package com.engwork.pgndb.chessgamesloader.mappers;

import com.engwork.pgndb.chessgamesconverter.model.Position;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PositionsMapper {

  @Insert({
      "INSERT INTO ",
      "positions",
      "(id,fen,white_pawns_count,black_pawns_count,white_pieces_count,black_pieces_count) ",
      "VALUES",
      "(#{id},#{fen},#{whitePawnsCount},#{blackPawnsCount},#{whitePiecesCount},#{blackPiecesCount})"
  })
  void insertPosition(Position position);

  @Insert({
      "<script>",
      "INSERT INTO positions",
      "(id,fen,white_pawns_count,black_pawns_count,white_pieces_count,black_pieces_count)",
      "VALUES <foreach item='item_name' collection='positions' open='' separator=',' close=''>",
      "(#{item_name.id},",
      "#{item_name.fen},",
      "#{item_name.whitePawnsCount},",
      "#{item_name.blackPawnsCount},",
      "#{item_name.whitePiecesCount},",
      "#{item_name.blackPiecesCount})",
      "</foreach>",
      " ON CONFLICT (id) DO NOTHING",
      "</script>"
  })
  void insertMany(@Param("positions") List<Position> positions);

  @Select("SELECT id FROM positions WHERE fen=#{fen}")
  UUID selectPositionId(Position position);

  @Select("SELECT id FROM positions WHERE fen=#{fen}")
  UUID selectPositionIdByFen(@Param("fen") String fen);

  @Select({
      "<script>",
      "SELECT * ",
      "FROM positions ",
      "WHERE id ",
      "IN <foreach item='item_name' collection='ids' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>",
      "</script>"
  })
  List<Position> selectPositionsByIds(@Param("ids") List<UUID> ids);

  @Select("SELECT * FROM positions ORDER BY id LIMIT #{limit} OFFSET #{offset}")
  List<Position> selectPositionsLimitOffset(@Param("limit") Integer limit, @Param("offset") Integer offset);

  @Select("SELECT count(*) FROM positions")
  Integer countPositions();

  @Select("SELECT * FROM positions WHERE fen LIKE #{fen} || '%'")
  List<Position> selectPositionsByFenRegex(@Param("fen") String fen);


  @Select({
      "<script>",
      "SELECT * ",
      "FROM positions ",
      "WHERE fen ",
      "IN <foreach item='item_name' collection='fens' open='(' separator=',' close=')'>",
      "#{item_name}",
      "</foreach>",
      "</script>"
  })
  List<Position> selectByFens(@Param("fens") List<String> fens);
}
