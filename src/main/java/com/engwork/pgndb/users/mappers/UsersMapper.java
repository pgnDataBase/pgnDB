package com.engwork.pgndb.users.mappers;

import com.engwork.pgndb.passwordencryption.EncryptedPassword;
import com.engwork.pgndb.users.User;
import com.engwork.pgndb.users.UserMetadata;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UsersMapper {

  @Insert({
      "INSERT INTO ",
      "users(id, username, password, salt) ",
      "VALUES ",
      "(#{id}, #{username}, #{password}, #{salt})"
  })
  void insertUser(User user);

  @Select("SELECT id, username FROM users WHERE username=#{username}")
  UserMetadata selectUserMetadataByUsername(@Param("username") String username);

  @Select("SELECT id, username FROM users WHERE id=#{id}")
  UserMetadata selectUserMetadataByUserId(@Param("id") UUID id);

  @Select("SELECT * FROM users WHERE username=#{username}")
  User selectUserByUsername(@Param("username") String username);

  @Select("SELECT password, salt FROM users WHERE username=#{username}")
  EncryptedPassword selectPasswordByUsername(@Param("username") String username);

  @Select("SELECT count(*) FROM users WHERE username=#{username}")
  Integer checkIfUserExists(@Param("username") String username);

  @Select("SELECT count(*) FROM users WHERE id=#{id}")
  Integer checkIfUserExistsById(@Param("id") UUID id);

  @Select("SELECT username FROM users WHERE  LOWER(username) LIKE '%' || LOWER(#{value}) || '%'")
  List<String> selectUsernamesRegex(@Param("value") String value);

  @Delete("DELETE FROM users WHERE username=#{username}")
  void deleteByUsername(@Param("username") String username);

}
