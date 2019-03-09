package com.engwork.pgndb.chessengine.engineinfo;

import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
public class EngineInfoManagingBeanImpl implements EngineInfoManagingBean{
  private final SqlSessionFactory sqlSessionFactory;

  public void insertEngineInfo(EngineInfo engineInfo){
    SqlSession sqlSession = sqlSessionFactory.openSession();
    EngineInfoMapper engineInfoMapper = sqlSession.getMapper(EngineInfoMapper.class);
    try {
      engineInfoMapper.insertEngineInfo(engineInfo);
      sqlSession.commit();
    }
    finally {
      sqlSession.close();
    }
  }

  public void deleteEngineInfo(){
    SqlSession sqlSession = sqlSessionFactory.openSession();
    EngineInfoMapper engineInfoMapper = sqlSession.getMapper(EngineInfoMapper.class);
    try {
      engineInfoMapper.deleteAll();
      sqlSession.commit();
    }
    finally {
      sqlSession.close();
    }
  }

  public List<EngineInfo> getEngineInfo(){
    List<EngineInfo> engineInfoList=null;
    SqlSession sqlSession = sqlSessionFactory.openSession();
    EngineInfoMapper engineInfoMapper = sqlSession.getMapper(EngineInfoMapper.class);
    try {
      engineInfoList = engineInfoMapper.selectEngineInfo();
    }
    finally {
      sqlSession.close();
    }
    return engineInfoList;
  }

}
