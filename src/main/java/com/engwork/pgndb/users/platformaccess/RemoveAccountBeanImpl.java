package com.engwork.pgndb.users.platformaccess;

import com.engwork.pgndb.chessdatabases.ChessDatabase;
import com.engwork.pgndb.chessdatabases.mappers.ChessDatabasesMapper;
import com.engwork.pgndb.databasemanager.DatabaseManager;
import com.engwork.pgndb.databasemanager.exceptions.UserDoesNotExistException;
import com.engwork.pgndb.databasesharing.DatabaseSharingBean;
import com.engwork.pgndb.passwordencryption.EncryptedPassword;
import com.engwork.pgndb.passwordencryption.PasswordEncryptionBean;
import com.engwork.pgndb.users.User;
import com.engwork.pgndb.users.UsersBean;
import com.engwork.pgndb.users.mappers.UsersMapper;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@Slf4j
@AllArgsConstructor
class RemoveAccountBeanImpl implements RemoveAccountBean {

  private final SqlSessionFactory sqlSessionFactory;
  private final PasswordEncryptionBean passwordEncryptionBean;
  private final DatabaseManager databaseManager;
  private final DatabaseSharingBean databaseSharingBean;
  private final UsersBean usersBean;

  @Override
  public boolean removeAccount(String username, String password) {
    if (!usersBean.checkIfUsernameExists(username)) {
      log.info("User with username [{}] doesn't exist.", username);
      throw new UserDoesNotExistException("Account removing", username);
    }

    User user;
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UsersMapper usersMapper = session.getMapper(UsersMapper.class);
      user = usersMapper.selectUserByUsername(username);
    }

    String realPassword = passwordEncryptionBean.decrypt(new EncryptedPassword(user.getPassword(), user.getSalt()));
    if (realPassword.equals(password)) {
      List<ChessDatabase> chessDatabaseList;
      try (SqlSession session = sqlSessionFactory.openSession()) {
        ChessDatabasesMapper chessDatabasesMapper = session.getMapper(ChessDatabasesMapper.class);
        chessDatabaseList = chessDatabasesMapper.selectChessDatabasesByUserId(user.getId());

        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        usersMapper.deleteByUsername(username);
      }
      chessDatabaseList.forEach(database -> databaseManager.delete(database.getName(), true));
      databaseSharingBean.removeUserAccesses(user.getId());
      return true;
    } else {
      return false;
    }


  }
}
