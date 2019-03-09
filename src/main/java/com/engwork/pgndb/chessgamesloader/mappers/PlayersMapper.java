package com.engwork.pgndb.chessgamesloader.mappers;

import com.engwork.pgndb.chessgamesconverter.model.Player;
import java.util.List;
import java.util.UUID;

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
public interface PlayersMapper {
  String insertPlayer = "insert into players(id,name) values (#{id},#{name})";
  String selectPlayerById = "select * from players where id=#{id}";
  String updatePlayer = "update players set name=#{name} where id=#{id}";

  @Select(selectPlayerById)
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "name", column = "name")
      }
  )
  Player selectPlayerById(UUID id);

  @Select("SELECT id FROM players WHERE name=#{name}")
  UUID selectPlayerIdByName(String name);

  @Insert(insertPlayer)
  void insertPlayer(Player player);

  @Update(updatePlayer)
  void updatePlayer(Player player);

  @Insert({
      "<script>",
      "INSERT INTO players",
      "(id, name)",
      "VALUES <foreach item='item_name' collection='players' open='' separator=',' close=''>",
      "(#{item_name.id},",
      "#{item_name.name})",
      "</foreach>",
      " ON CONFLICT (id) DO NOTHING",
      "</script>"
  })
  void insertMany(@Param("players") List<Player> players);

  //  SEARCHING
  @Select("SELECT id FROM players WHERE LOWER(name) LIKE '%' || LOWER(#{name}) || '%'")
  List<UUID> selectPlayerIdByNameWithRegex(String name);

}
