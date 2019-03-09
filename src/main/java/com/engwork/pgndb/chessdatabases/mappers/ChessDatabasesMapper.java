package com.engwork.pgndb.chessdatabases.mappers;

import com.engwork.pgndb.chessdatabases.ChessDatabase;
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
public interface ChessDatabasesMapper {

  @Select("SELECT id, name FROM chess_databases")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "name", column = "name")
      }
  )
  List<ChessDatabase> selectChessDatabases();

  @Select("SELECT id, name FROM chess_databases WHERE name=#{name}")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "name", column = "name")
      }
  )
  ChessDatabase selectChessDatabasesByName(@Param("name") String name);

  @Select("SELECT id, name FROM chess_databases WHERE id=#{id}")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "name", column = "name")
      }
  )
  ChessDatabase selectChessDatabasesById(@Param("id") UUID id);

  @Select("SELECT id, name FROM chess_databases WHERE user_id=#{userId}")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "name", column = "name")
      }
  )
  List<ChessDatabase> selectChessDatabasesByUserId(@Param("userId") UUID userId);

  @Select("SELECT id, name FROM chess_databases WHERE id=#{id} AND user_id=#{userId}")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "name", column = "name")
      }
  )
  ChessDatabase selectChessDatabaseByUserIdAndDatabaseId(@Param("userId") UUID userId, @Param("id") UUID databaseId);

  @Insert({
      "INSERT INTO ",
      "chess_databases ",
      "(id, user_id, name) ",
      "VALUES(#{id}, #{userId}, #{databaseName})"
  })
  void insertChessDatabase(@Param("id") UUID id, @Param("databaseName") String databaseName, @Param("userId") UUID userId);

  @Delete("DELETE FROM chess_databases WHERE name=#{name}")
  void deleteChessDatabase(@Param("name") String databaseName);


}
