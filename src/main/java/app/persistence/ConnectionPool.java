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

    private ConnectionPool()
    {
    }

    private static String requiredEnv(String key) {
        String val = System.getenv(key);
        if (val == null || val.isBlank()) {
            throw new IllegalStateException("Missing required env var: " + key);
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
        ds.close();
    }

    private static HikariDataSource createHikariConnectionPool() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(requiredEnv("JDBC_URL"));
        config.setUsername(requiredEnv("JDBC_USER"));
        config.setPassword(requiredEnv("JDBC_PASSWORD"));

        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}