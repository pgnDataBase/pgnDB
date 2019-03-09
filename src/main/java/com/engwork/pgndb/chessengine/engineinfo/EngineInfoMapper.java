package com.engwork.pgndb.chessengine.engineinfo;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EngineInfoMapper {
  @Select("select * from engine_info order by depth desc")
  @Results(
      value = {
          @Result(property = "fen", column = "fen"),
          @Result(property = "depth", column = "depth"),
          @Result(property = "scoreCentiPawn", column = "score_in_centi_pawn"),
          @Result(property = "time", column = "processing_time"),
          @Result(property = "movesChain", column = "moves_chain")
      }
  )
  List<EngineInfo> selectEngineInfo();

  @Delete("delete from engine_info")
  void deleteAll();

  @Insert("insert into engine_info(fen,depth,score_in_centi_pawn,processing_time,moves_chain) values(#{fen},#{depth},#{scoreCentiPawn},#{time},#{movesChain})")
  void insertEngineInfo(EngineInfo engineInfo);
}

