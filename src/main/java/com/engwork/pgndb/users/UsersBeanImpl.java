package com.engwork.pgndb.users;

import com.engwork.pgndb.tagssearching.SortWithStartValueBean;
import com.engwork.pgndb.users.mappers.UsersMapper;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class UsersBeanImpl implements UsersBean {

  private final SqlSessionFactory sqlSessionFactory;
  private final SortWithStartValueBean sortWithStartValueBean;

  @Override
  public UserMetadata getUserMetadataByUsername(String username) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      return session.getMapper(UsersMapper.class).selectUserMetadataByUsername(username);
    }
  }

  @Override
  public UserMetadata getUserMetadataByUserId(UUID userId) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      return session.getMapper(UsersMapper.class).selectUserMetadataByUserId(userId);
    }
  }

  @Override
  public boolean checkIfUsernameExists(String username) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UsersMapper usersMapper = session.getMapper(UsersMapper.class);
      Integer result = usersMapper.checkIfUserExists(username);
      return result > 0;
    }
  }

  @Override
  public boolean checkIfUserExists(UUID id) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UsersMapper usersMapper = session.getMapper(UsersMapper.class);
      Integer result = usersMapper.checkIfUserExistsById(id);
      return result > 0;
    }
  }

  @Override
  public List<String> getUsersRegex(String value) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UsersMapper usersMapper = session.getMapper(UsersMapper.class);
      return sortWithStartValueBean.sort(usersMapper.selectUsernamesRegex(value), value);
    }
  }
}
