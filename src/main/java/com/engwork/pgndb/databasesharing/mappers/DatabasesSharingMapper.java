package com.engwork.pgndb.databasesharing.mappers;

import com.engwork.pgndb.databasesharing.DatabaseAccess;
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
public interface DatabasesSharingMapper {

  @Insert({
      "INSERT INTO ",
      "chess_databases_sharing",
      "(owner_id, user_id, database_id) ",
      "VALUES(#{ownerId},#{userId}, #{databaseId}) ",
      "ON CONFLICT DO NOTHING"
  })
  void addAccess(DatabaseAccess databaseAccess);

  @Select("SELECT * FROM chess_databases_sharing WHERE user_id=#{userId}")
  @Results(
      value = {
          @Result(property = "ownerId", column = "owner_id"),
          @Result(property = "databaseId", column = "database_id"),
          @Result(property = "userId", column = "user_id")
      }
  )
  List<DatabaseAccess> selectByUserId(@Param("userId") UUID userId);

  @Select("SELECT * FROM chess_databases_sharing WHERE database_id=#{databaseId}")
  @Results(
      value = {
          @Result(property = "ownerId", column = "owner_id"),
          @Result(property = "databaseId", column = "database_id"),
          @Result(property = "userId", column = "user_id")
      }
  )
  List<DatabaseAccess> selectByDatabaseId(@Param("databaseId") UUID databaseId);

  @Delete("DELETE FROM chess_databases_sharing WHERE database_id=#{databaseId}")
  void deleteDatabaseAccessByDatabaseId(@Param("databaseId") UUID databaseId);

  @Delete("DELETE FROM chess_databases_sharing WHERE owner_id=#{ownerId} OR user_id=#{ownerId}")
  void deleteDatabaseAccessesByOwnerIdOrUserId(@Param("ownerId") UUID id);

  @Delete("DELETE FROM chess_databases_sharing WHERE database_id=#{databaseId} AND user_id=#{userId}")
  void deleteDatabaseAccess(@Param("databaseId") UUID databaseId, @Param("userId") UUID userId);

}
