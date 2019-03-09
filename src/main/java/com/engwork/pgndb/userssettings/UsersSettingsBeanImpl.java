package com.engwork.pgndb.userssettings;

import com.engwork.pgndb.userssettings.mappers.UsersSettingsMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class UsersSettingsBeanImpl implements UsersSettingsBean {

  private final SqlSessionFactory sqlSessionFactory;
  private final UsersSettingsDaoMapperBean usersSettingsDaoMapperBean;

  @Override
  public void createSettingsByUserId(Map<String, String> settings, UUID userId) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    UsersSettingsMapper usersSettingsMapper = sqlSession.getMapper(UsersSettingsMapper.class);
    try {
      List<UserSettingsDao> dataToUpsert = usersSettingsDaoMapperBean.toDao(
          settings, userId
      );
      usersSettingsMapper.upsertMany(dataToUpsert);
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }

  @Override
  public Map<String, String> getSettingsByUserId(UUID userId) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      UsersSettingsMapper usersSettingsMapper = sqlSession.getMapper(UsersSettingsMapper.class);
      List<UserSettingsDao> result = usersSettingsMapper.selectConfigurationByUserId(userId);
      return usersSettingsDaoMapperBean.fromDao(result);
    }
  }
}
