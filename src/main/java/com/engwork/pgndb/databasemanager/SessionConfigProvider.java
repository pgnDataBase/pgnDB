package com.engwork.pgndb.databasemanager;

import org.apache.ibatis.session.Configuration;

public interface SessionConfigProvider {
  Configuration getConfig();
}
