package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.PgnDbApplication;
import com.engwork.pgndb.chessdatabases.ChessDatabase;
import com.engwork.pgndb.chessdatabases.ChessDatabasesManagementBean;
import com.engwork.pgndb.chessgames.ChessGamesBean;
import com.engwork.pgndb.users.UserMetadata;
import com.engwork.pgndb.users.UsersBean;
import com.engwork.pgndb.users.platformaccess.PlatformAccessBean;
import com.engwork.pgndb.users.platformaccess.RemoveAccountBean;
import com.engwork.pgndb.users.requestmodel.SignUpRequest;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Ignore(value = "This tests require database running(integration test)")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {PgnDbApplication.class},
    initializers = ConfigFileApplicationContextInitializer.class
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DatabaseManagerTest {

  @Autowired
  DatabaseManager databaseManager;

  @Autowired
  DatabaseContainer databaseContainer;

  @Autowired
  ChessDatabasesManagementBean chessDatabasesManagementBean;

  @Autowired
  ChessGamesBean chessGamesBean;

  @Autowired
  PlatformAccessBean platformAccessBean;

  @Autowired
  UsersBean usersBean;

  @Autowired
  RemoveAccountBean removeAccountBean;

  @Test
  void testCreateDatabase() {

    // given
    String id = Integer.toString(ThreadLocalRandom.current().nextInt(0, 100));
    String databaseName = "test-database-" + id;
    String username = "test-user-" + id;
    String password = "test-password";

    // when
    platformAccessBean.signUp(new SignUpRequest(username, password));
    UserMetadata userMetadata = usersBean.getUserMetadataByUsername(username);
    UUID userId = userMetadata.getId();

    databaseManager.create(databaseName, userId);

    // then
    boolean databaseInMainDb = false;
    boolean databaseActuallyExists = false;
    boolean databaseInDatabaseContainer;

    List<ChessDatabase> chessDatabases = chessDatabasesManagementBean.getChessDatabases();

    for (ChessDatabase chessDatabase : chessDatabases) {
      if (databaseName.equals(chessDatabase.getName())) {
        databaseInMainDb = true;
      }
    }
    Assertions.assertTrue(databaseInMainDb);

    Integer numberOfChessGamesInDatabase = chessGamesBean.countChessGames(databaseName);
    if (numberOfChessGamesInDatabase >= 0) {
      databaseActuallyExists = true;
    }
    Assertions.assertTrue(databaseActuallyExists);

    databaseInDatabaseContainer = databaseContainer.getAvailableDatabases().contains(databaseName);
    Assertions.assertTrue(databaseInDatabaseContainer);

    removeAccountBean.removeAccount(username, password);
  }

  @Test
  void testCreateDatabaseManyTimes() {
    int repeats = 100;
    for (int index = 0; index < repeats; index++) {
      testCreateDatabase();
    }
  }


  @Test
  void testDeleteDatabase() {
    // given
    String id = Integer.toString(ThreadLocalRandom.current().nextInt(0, 100));
    String databaseName = "test-database-" + id;
    String username = "test-user-" + id;
    String password = "test-password";

    // when
    platformAccessBean.signUp(new SignUpRequest(username, password));
    UserMetadata userMetadata = usersBean.getUserMetadataByUsername(username);
    UUID userId = userMetadata.getId();

    databaseManager.create(databaseName, userId);

    // when
    databaseManager.delete(databaseName, true);

    // then
    boolean databaseInMainDb = true;
    boolean databaseInDatabaseContainer;

    List<ChessDatabase> chessDatabases = chessDatabasesManagementBean.getChessDatabases();

    for (ChessDatabase chessDatabase : chessDatabases) {
      if (databaseName.equals(chessDatabase.getName())) {
        databaseInMainDb = false;
      }
    }
    Assertions.assertTrue(databaseInMainDb);

    databaseInDatabaseContainer = databaseContainer.getAvailableDatabases().contains(databaseName);
    Assertions.assertFalse(databaseInDatabaseContainer);

    removeAccountBean.removeAccount(username, password);
  }
}
