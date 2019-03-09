package com.engwork.pgndb.chessgamesloader;

import java.util.Set;

public interface TagsLoaderBean {
  void insertMany(Set<String> tags, String databaseName);
}
