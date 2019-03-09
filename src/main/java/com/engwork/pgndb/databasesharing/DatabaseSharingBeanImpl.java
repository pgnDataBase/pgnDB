package com.engwork.pgndb.databasesharing;

import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.accessauthorization.OwnershipAuthorizationBean;
import com.engwork.pgndb.databasesharing.exceptions.SharingExceptionsFactory;
import com.engwork.pgndb.databasesharing.mappers.DatabasesSharingMapper;
import com.engwork.pgndb.users.UserMetadata;
import com.engwork.pgndb.users.UsersBean;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
public class DatabaseSharingBeanImpl implements DatabaseSharingBean {

  private final SqlSessionFactory sqlSessionFactory;
  private final UsersBean usersBean;

  private final OwnershipAuthorizationBean ownershipAuthorizationBean;
  private final SharingExceptionsFactory sharingExceptionsFactory;
  private final AccessExceptionsFactory accessExceptionsFactory;

  @Override
  public boolean hasAccessTo(UUID userId, UUID databaseId) {
    List<DatabaseAccess> accesses;
    try (SqlSession session = sqlSessionFactory.openSession()) {
      DatabasesSharingMapper databasesSharingMapper = session.getMapper(DatabasesSharingMapper.class);
      accesses = databasesSharingMapper.selectByUserId(userId);
    }
    return accesses.stream().map(DatabaseAccess::getDatabaseId).anyMatch(databaseId::equals);
  }

  @Override
  public void addAccess(UUID ownerId, UUID databaseId, String username) {
    UserMetadata userMetadata = usersBean.getUserMetadataByUsername(username);
    if (userMetadata == null) {
      throw sharingExceptionsFactory.userNotExistsWithUsername(username);
    }
    if (!ownershipAuthorizationBean.validateAccess(ownerId, databaseId)) {
      throw accessExceptionsFactory.notOwnerWithId(databaseId);
    }
    if (ownerId.equals(userMetadata.getId())) {
      throw sharingExceptionsFactory.cannotShareWithYourself(ownerId);
    }

    DatabaseAccess databaseAccess = new DatabaseAccess(ownerId, databaseId, userMetadata.getId());
    try (SqlSession session = sqlSessionFactory.openSession()) {
      DatabasesSharingMapper databasesSharingMapper = session.getMapper(DatabasesSharingMapper.class);
      databasesSharingMapper.addAccess(databaseAccess);
      session.commit();
    }
  }

  @Override
  public void removeAccess(UUID databaseId, String username) {
    UserMetadata userMetadata = usersBean.getUserMetadataByUsername(username);
    if (userMetadata == null) {
      throw sharingExceptionsFactory.userNotExistsWithUsername(username);
    }

    try (SqlSession session = sqlSessionFactory.openSession()) {
      DatabasesSharingMapper databasesSharingMapper = session.getMapper(DatabasesSharingMapper.class);
      databasesSharingMapper.deleteDatabaseAccess(databaseId, userMetadata.getId());
      session.commit();
    }
  }

  @Override
  public void removeAccesses(UUID databaseId) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      if (databaseId != null) {
        DatabasesSharingMapper databasesSharingMapper = session.getMapper(DatabasesSharingMapper.class);
        databasesSharingMapper.deleteDatabaseAccessByDatabaseId(databaseId);
        session.commit();
      }
    }
  }

  @Override
  public void removeUserAccesses(UUID ownerId) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      DatabasesSharingMapper databasesSharingMapper = session.getMapper(DatabasesSharingMapper.class);
      databasesSharingMapper.deleteDatabaseAccessesByOwnerIdOrUserId(ownerId);
      session.commit();
    }
  }

  public List<String> getUsersWithAccess(UUID ownerId, UUID databaseId) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      DatabasesSharingMapper databasesSharingMapper = session.getMapper(DatabasesSharingMapper.class);

      List<DatabaseAccess> databaseAccesses = databasesSharingMapper.selectByDatabaseId(databaseId);

      databaseAccesses.forEach(databaseAccess -> {
        if (!ownerId.equals(databaseAccess.getOwnerId())) {
          throw accessExceptionsFactory.notOwnerWithId(databaseAccess.getOwnerId());
        }
      });

      return databaseAccesses
          .stream()
          .map(databaseAccess -> usersBean.getUserMetadataByUserId(databaseAccess.getUserId()).getUsername())
          .collect(Collectors.toList());
    }
  }
}
