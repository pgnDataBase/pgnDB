package com.engwork.pgndb.databasemanager;


import com.engwork.pgndb.databasemanager.exceptions.DatabaseDoNotExistException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseExistsException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseNotChosenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseContainerInjector.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DatabaseContainerTest {

  @Autowired
  DatabaseContainer databaseContainer;

  @Test
  void testAddDatabaseKey() {
    // given
    String databaseName = "test-database-name";

    // when
    databaseContainer.addDatabaseKey(databaseName);
    databaseContainer.setDatabaseKey(databaseName);

    // then
    String actual = databaseContainer.getActiveDatabase();
    Assertions.assertEquals(databaseName, actual);
  }

  @Test
  void testAddDatabaseKeyThatExists() {
    // given
    String databaseName = "test-database-name";

    // when
    databaseContainer.addDatabaseKey(databaseName);


    // then
    Assertions.assertThrows(
        DatabaseExistsException.class,
        () -> databaseContainer.addDatabaseKey(databaseName)
    );
  }

  @Test
  void testRemoveDatabaseKey() {
    // given
    String databaseName = "test-database-name";

    // when
    databaseContainer.addDatabaseKey(databaseName);
    databaseContainer.setDatabaseKey(databaseName);

    // then
    databaseContainer.removeDatabaseKey(databaseName);
    Assertions.assertThrows(
        DatabaseNotChosenException.class,
        () -> databaseContainer.getActiveDatabase()
    );
    Assertions.assertThrows(
        DatabaseDoNotExistException.class,
        () -> databaseContainer.setDatabaseKey(databaseName)
    );
  }

  @Test
  void testRemoveDatabaseKeyThatDoNotExist() {
    // given
    String databaseName = "non-existing-test-database-name-1";

    // when

    // then
    Assertions.assertThrows(
        DatabaseDoNotExistException.class,
        () -> databaseContainer.removeDatabaseKey(databaseName)
    );
  }

  @Test
  void testMultiThreadDatabaseContainerUsage() {

    // given
    Integer expectedDatabasesNumber = 0;
    int databasesNumber = 1000;
    ArrayList<String> databaseNames = new ArrayList<>();
    for (int counter = 0; counter < databasesNumber; counter++) {
      databaseNames.add("test-database-name-" + counter);
    }

    databaseNames.parallelStream().forEach(databaseName -> {
      // when
      databaseContainer.addDatabaseKey(databaseName);
      databaseContainer.setDatabaseKey(databaseName);

      // then
      String actual = databaseContainer.getActiveDatabase();
      Assertions.assertEquals(databaseName, actual);
      databaseContainer.removeDatabaseKey(databaseName);
    });

    // then
    List<String> actualDatabases = databaseContainer.getAvailableDatabases();
    Integer actualDatabasesNumber = actualDatabases.size();
    Assertions.assertEquals(expectedDatabasesNumber, actualDatabasesNumber);
  }


}
