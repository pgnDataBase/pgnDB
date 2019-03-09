package com.engwork.pgndb.databasemanager;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@Slf4j
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(UUID.class)
public class UUIDTypeHandler implements TypeHandler<UUID> {

  @Override
  public void setParameter(PreparedStatement preparedStatement, int parameterIndex, UUID parameter, JdbcType jdbcType) throws SQLException {
    if (parameter == null) {
      preparedStatement.setObject(parameterIndex, null, Types.OTHER);
    } else {
      preparedStatement.setObject(parameterIndex, parameter.toString(), Types.OTHER);
    }

  }

  @Override
  public UUID getResult(ResultSet resultSet, String columnName) throws SQLException {
    return toUUID(resultSet.getString(columnName));
  }

  @Override
  public UUID getResult(ResultSet resultSet, int columnIndex) throws SQLException {
    return toUUID(resultSet.getString(columnIndex));
  }

  @Override
  public UUID getResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
    return toUUID(callableStatement.getString(columnIndex));
  }

  private static UUID toUUID(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException exception) {
      log.warn("Bad UUID found: {}", value);
    }
    return null;
  }

}