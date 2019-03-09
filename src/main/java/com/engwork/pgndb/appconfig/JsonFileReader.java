package com.engwork.pgndb.appconfig;

import org.json.JSONObject;

public interface JsonFileReader {
  JSONObject read(String fileName);
}
