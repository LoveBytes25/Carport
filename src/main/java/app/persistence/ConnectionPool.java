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

    private static HikariDataSource createHikariConnectionPool()
    {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(System.getenv("JDBC_URL"));
        config.setUsername(System.getenv("JDBC_USER"));
        config.setPassword(System.getenv("JDBC_PASSWORD"));

        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}