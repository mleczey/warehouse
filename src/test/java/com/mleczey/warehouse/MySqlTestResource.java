package com.mleczey.warehouse;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.testcontainers.containers.MySQLContainer;

public class MySqlTestResource implements QuarkusTestResourceLifecycleManager {

  private final MySQLContainer MYSQL_DATABASE = new MySQLContainer<>("mysql:8.0.21")
      .withUsername("root")
      .withPassword("")
      .withDatabaseName("warehouse");

  @Override
  public Map<String, String> start() {
    MYSQL_DATABASE.start();
    final var jdbcUrl = MYSQL_DATABASE.getJdbcUrl();
    final var username = MYSQL_DATABASE.getUsername();
    final var password = MYSQL_DATABASE.getPassword();

    try (final var connection = DriverManager.getConnection(jdbcUrl, username, password)) {
      final var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
      final var liquibase = new Liquibase("db/changeLog.xml", new FileSystemResourceAccessor(), database);
      liquibase.update(new Contexts());
    } catch (SQLException | LiquibaseException x) {
    }

    final var properties = new HashMap<String, String>();
    properties.put("quarkus.datasource.url", jdbcUrl);
    properties.put("quarkus.datasource.username", username);
    properties.put("quarkus.datasource.password", password);
    return properties;
  }

  @Override
  public void stop() {
    MYSQL_DATABASE.close();
  }

}
