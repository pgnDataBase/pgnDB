package com.engwork.pgndb.appconfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

@Data
@Slf4j
class JsonFileReaderImpl implements JsonFileReader {

  @Override
  public JSONObject read(String fileName) {
    try {
      String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));
      return new JSONObject(jsonString);
    } catch (Exception exception) {
      log.error("Cannot read json file {}!", fileName);
      throw new JsonFileReadingException(exception);
    }
  }
}
