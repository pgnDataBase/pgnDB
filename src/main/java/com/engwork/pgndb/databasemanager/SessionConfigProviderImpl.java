package com.engwork.pgndb.databasemanager;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

class SessionConfigProviderImpl implements SessionConfigProvider {
  public Configuration getConfig() {
    Configuration config = new Configuration();
    TypeHandlerRegistry handlers = config.getTypeHandlerRegistry();
    handlers.register(UUIDTypeHandler.class);
    return config;
  }
}
