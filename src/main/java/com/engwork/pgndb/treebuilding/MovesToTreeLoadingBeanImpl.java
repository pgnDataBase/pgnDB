package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.treebuilding.mapper.TreeMapper;
import com.engwork.pgndb.treebuilding.treenodescalculator.TreeNodesCalculatorBean;
import com.engwork.pgndb.treebuilding.treenodesmerging.TreeNodesMergingBean;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class MovesToTreeLoadingBeanImpl implements MovesToTreeLoadingBean {

  private static final Integer CHUNK_SIZE = 500;

  private final SqlSessionFactory sqlSessionFactory;
  private final TreeNodesCalculatorBean treeNodesCalculatorBean;
  private final TreeNodesMergingBean treeNodesMergingBean;

  @Override
  @SuppressWarnings("Duplicates")
  public void load(List<Move> moves, Boolean includeVariants) {

    List<TreeNode> newTreeNodes = this.treeNodesCalculatorBean.calculate(moves, includeVariants);
    List<TreeNode> oldTreeNodes;

    try (SqlSession session = sqlSessionFactory.openSession()) {
      TreeMapper treeMapper = session.getMapper(TreeMapper.class);
      oldTreeNodes = treeMapper.selectEqualTreeNodes(newTreeNodes);
    }

    oldTreeNodes = oldTreeNodes.stream().filter(newTreeNodes::contains).collect(Collectors.toList());

    List<TreeNode> mergedTreeNodes = treeNodesMergingBean.merge(oldTreeNodes, newTreeNodes);
    List<TreeNode> chunk = new ArrayList<>();

    SqlSession sqlSession = sqlSessionFactory.openSession();
    TreeMapper treeMapper = sqlSession.getMapper(TreeMapper.class);
    try {
      for (int index = 0; index < mergedTreeNodes.size(); index++) {
        chunk.add(mergedTreeNodes.get(index));
        boolean isChunkFull = chunk.size() == CHUNK_SIZE;
        boolean areElementsProcessedAndChunkNotEmpty = (index == mergedTreeNodes.size() - 1) && !chunk.isEmpty();
        if (isChunkFull || areElementsProcessedAndChunkNotEmpty) {
          treeMapper.upsertMany(chunk);
          chunk.clear();
        }
      }
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }
}
