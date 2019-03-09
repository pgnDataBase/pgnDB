package com.engwork.pgndb.treebuilding.mapper;

import com.engwork.pgndb.treebuilding.TreeNode;
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
public interface TreeMapper {

  @Select("SELECT * FROM tree WHERE start_position_id=#{startPositionId}")
  @Results(
      value = {
          @Result(property = "startPositionId", column = "start_position_id"),
          @Result(property = "finalPositionId", column = "final_position_id"),
          @Result(property = "moveSan", column = "move_san"),
          @Result(property = "moveCount", column = "move_count")
      }
  )
  List<TreeNode> selectTreeNodeByStartPositionId(UUID startPositionId);

  @Select({
      "<script>",
      "SELECT * ",
      "FROM tree ",
      "WHERE start_position_id ",
      "IN <foreach item='item_name' collection='nodes' open='(' separator=',' close=')'>",
      "#{item_name.startPositionId} </foreach>",
      "and move_san ",
      "IN <foreach item='item_name' collection='nodes' open='(' separator=',' close=')'>",
      "#{item_name.moveSan}",
      "</foreach>",
      "</script>"
  })
  @Results(
      value = {
          @Result(property = "startPositionId", column = "start_position_id"),
          @Result(property = "finalPositionId", column = "final_position_id"),
          @Result(property = "moveSan", column = "move_san"),
          @Result(property = "moveCount", column = "move_count")
      }
  )
  List<TreeNode> selectEqualTreeNodes(@Param("nodes") List<TreeNode> treeNodes);


  @Insert({
      "<script>",
      "INSERT INTO tree",
      "(start_position_id,final_position_id,move_san,move_count)",
      "VALUES <foreach item='item_name' collection='nodes' open='' separator=',' close=''>",
      "(#{item_name.startPositionId},",
      "#{item_name.finalPositionId},",
      "#{item_name.moveSan},",
      "#{item_name.moveCount})",
      "</foreach>",
      "ON CONFLICT (start_position_id,move_san) DO UPDATE SET move_count =excluded.move_count",
      "</script>"
  })
  void upsertMany(@Param("nodes") List<TreeNode> treeNodes);

  @Delete("DELETE FROM tree")
  void deleteTree();

}
