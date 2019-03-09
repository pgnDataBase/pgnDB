package com.engwork.pgndb.chessgamesloader.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TagsMapper {

  @Insert({
      "<script>",
      "INSERT INTO tags",
      "(name)",
      "VALUES <foreach item='item' collection='tags' open='' separator=',' close=''>",
      "(#{item})",
      "</foreach>",
      " ON CONFLICT (name) DO NOTHING",
      "</script>"
  })
  void insertMany(@Param("tags") List<String> tags);

  //  SEARCHING
  @Select("SELECT name FROM tags WHERE LOWER(name) LIKE '%' || LOWER(#{name}) || '%'")
  List<String> selectTagsWithRegex(String name);

}
