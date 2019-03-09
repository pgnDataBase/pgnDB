package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.chessdatabases.ChessDatabasesManagementBean;
import com.engwork.pgndb.databasemanager.exceptions.PSQLManagerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class PSQLManagerImpl implements PSQLManager {

  private static final String CREATE_DATABASE_SCRIPT = "src/main/resources/scripts/database-create.sh";
  private static final String DROP_DATABASE_SCRIPT = "src/main/resources/scripts/database-drop.sh";
  private static final String DROP_DATABASE_FORCE_SCRIPT = "src/main/resources/scripts/database-drop-force.sh";
  private static final String BASH_COMMAND = "/bin/bash";
  private static final String SYSTEM_CURRENT_DIR_PROPERTY = "user.dir";

  private final ChessDatabasesManagementBean chessDatabasesManagementBean;

  private class ScriptLogger implements Runnable {
    private final InputStream inputStream;
    private final Consumer<String> consumer;

    ScriptLogger(InputStream inputStream, Consumer<String> consumer) {
      this.inputStream = inputStream;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      new BufferedReader(new InputStreamReader(inputStream))
          .lines()
          .forEach(consumer);
    }

    String readStream() {
      return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
    }
  }

  private void executeScript(String scriptPath, String databaseName) {
    try {
      File file = new File(scriptPath);
      String absolutePath = file.getAbsolutePath();

      ProcessBuilder builder = new ProcessBuilder();
      builder.command(BASH_COMMAND, absolutePath, databaseName.trim());
      builder.directory(new File(System.getProperty(SYSTEM_CURRENT_DIR_PROPERTY)));

      Process process = builder.start();
      log.info("Script " + scriptPath.substring(scriptPath.lastIndexOf("/") + 1) + " started");
      ScriptLogger scriptLogger = new ScriptLogger(process.getErrorStream(), System.out::println);
//      Executors.newSingleThreadExecutor().submit(scriptLogger);

      process.waitFor();
      int exitValue = process.exitValue();
      if (exitValue != 0) {
        throw new PSQLManagerException(scriptLogger.readStream());
      }
    } catch (IOException | InterruptedException exception) {
      throw new PSQLManagerException(exception.getMessage());
    }
  }

  @Override
  public void createDatabase(String databaseName, UUID userId) {
    executeScript(CREATE_DATABASE_SCRIPT, databaseName);
    chessDatabasesManagementBean.insertChessDatabase(databaseName, userId);
  }

  @Override
  public void deleteDatabase(String databaseName, boolean force) {
    if (force) {
      executeScript(DROP_DATABASE_FORCE_SCRIPT, databaseName);
    } else {
      executeScript(DROP_DATABASE_SCRIPT, databaseName);
    }
    chessDatabasesManagementBean.deleteChessDatabase(databaseName);
  }
}
