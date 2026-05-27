package app.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPool
{
    public static ConnectionPool instance = null;
    public static HikariDataSource ds = null;

    private ConnectionPool() {}

    private static String config(String key) {
        // 1. env vars (Docker / Linux / CI)
        String val = System.getenv(key);

        // 2. JVM system properties (IntelliJ / Maven -D)
        if (val == null || val.isBlank()) {
            val = System.getProperty(key);
        }

        // 3. final validation
        if (val == null || val.isBlank()) {
            throw new IllegalStateException("Missing config: " + key);
        }

        return val;
    }

    public static ConnectionPool getInstance()
    {
        if (instance == null)
        {
            ds = createHikariConnectionPool();
            instance = new ConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException
    {
        return ds.getConnection();
    }

    public synchronized void close()
    {
        Logger.getLogger("web").log(Level.INFO, "Shutting down connection pool");
        if (ds != null) ds.close();
    }

    private static HikariDataSource createHikariConnectionPool()
    {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(config("JDBC_URL"));
        config.setUsername(config("JDBC_USER"));
        config.setPassword(config("JDBC_PASSWORD"));

        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}