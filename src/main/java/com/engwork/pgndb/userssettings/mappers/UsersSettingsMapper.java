package com.engwork.pgndb.userssettings.mappers;

import com.engwork.pgndb.userssettings.UserSettingsDao;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UsersSettingsMapper {

  @Select("select * from users_settings where user_id=#{userId}")
  @Results(
      value = {
          @Result(property = "userId", column = "user_id"),
          @Result(property = "key", column = "key"),
          @Result(property = "value", column = "value")
      }
  )
  List<UserSettingsDao> selectConfigurationByUserId(@Param("userId") UUID userId);

  @Insert({
      "<script>",
      "INSERT INTO users_settings",
      "(user_id,key,value)",
      "VALUES <foreach item='item_name' collection='userSettingsDaos' open='' separator=',' close=''>",
      "(#{item_name.userId},",
      "#{item_name.key},",
      "#{item_name.value})",
      "</foreach>",
      " ON CONFLICT (user_id,key) DO UPDATE SET value=excluded.value",
      "</script>"
  })
  void upsertMany(@Param("userSettingsDaos") List<UserSettingsDao> userSettingsDaos);

}
