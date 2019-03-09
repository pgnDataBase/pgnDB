package com.engwork.pgndb.users.platformaccess;

import com.engwork.pgndb.passwordencryption.EncryptedPassword;
import com.engwork.pgndb.passwordencryption.PasswordEncryptionBean;
import com.engwork.pgndb.security.JWTGenerator;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.User;
import com.engwork.pgndb.users.UsersBean;
import com.engwork.pgndb.users.mappers.UsersMapper;
import com.engwork.pgndb.users.profilevalidation.ProfileValidationBean;
import com.engwork.pgndb.users.requestmodel.SignUpRequest;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class PlatformAccessBeanImpl implements PlatformAccessBean {

  private final SqlSessionFactory sqlSessionFactory;

  private final JWTGenerator jwtGenerator;
  private final JWTValidator jwtValidator;

  private final UsersBean usersBean;
  private final PasswordEncryptionBean passwordEncryptionBean;
  private final ProfileValidationBean profileValidationBean;

  @Override
  public void signUp(SignUpRequest signUpRequest) {
    profileValidationBean.validateUsername(signUpRequest.getUsername());
    profileValidationBean.validatePassword(signUpRequest.getPassword());

    EncryptedPassword encryptedPassword = passwordEncryptionBean.encrypt(signUpRequest.getPassword());
    User user = new User(
        UUID.randomUUID(),
        signUpRequest.getUsername(),
        encryptedPassword.getPassword(),
        encryptedPassword.getSalt()
    );

    try (SqlSession session = sqlSessionFactory.openSession()) {
      UsersMapper usersMapper = session.getMapper(UsersMapper.class);
      usersMapper.insertUser(user);
    }
  }

  @Override
  public String signIn(String username, String password) {
    if (!(usersBean.checkIfUsernameExists(username))) {
      return null;
    }

    EncryptedPassword encryptedPassword;
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UsersMapper usersMapper = session.getMapper(UsersMapper.class);
      encryptedPassword = usersMapper.selectPasswordByUsername(username);
    }

    String realPassword = passwordEncryptionBean.decrypt(encryptedPassword);
    if (realPassword.equals(password)) {
      try (SqlSession session = sqlSessionFactory.openSession()) {
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        User user = usersMapper.selectUserByUsername(username);
        return jwtGenerator.generate(user);
      }
    }
    return null;
  }

  @Override
  public void signOut(String token) {
    jwtValidator.deactivateToken(token);
  }

  @Override
  public String generateTokenForUser(String username) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UsersMapper usersMapper = session.getMapper(UsersMapper.class);
      User user = usersMapper.selectUserByUsername(username);
      return jwtGenerator.generate(user);
    }
  }
}
