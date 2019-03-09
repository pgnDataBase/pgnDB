package com.engwork.pgndb.treebuilding.mapper;

import com.engwork.pgndb.treebuilding.treestatus.TreeStatus;
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
public interface TreeStatusMapper {

  @Insert({
      "INSERT INTO tree_status ",
      "(is_tree_up_to_date,is_tree_with_variants) ",
      "VALUES",
      "(#{isTreeUpToDate},#{isTreeWithVariants})"
  })
  void insertTreeStatus(TreeStatus treeStatus);

  @Select("SELECT * FROM tree_status")
  @Results(
      value = {
          @Result(property = "isTreeUpToDate", column = "is_tree_up_to_date"),
          @Result(property = "isTreeWithVariants", column = "is_tree_with_variants"),
      }
  )
  TreeStatus selectTreeStatus();

  @Select("SELECT count(*) FROM tree_status")
  Integer countTreeStatuses();

  @Update("UPDATE tree_status SET is_tree_up_to_date=#{isTreeUpToDate}")
  void updateUpToDateFlag(@Param("isTreeUpToDate") Boolean isTreeUpToDate);

  @Delete("DELETE FROM tree_status")
  void deleteTreeStatus();

}
