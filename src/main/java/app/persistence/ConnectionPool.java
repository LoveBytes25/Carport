package app.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool
{
    private static ConnectionPool instance;
    private static HikariDataSource ds;

    private ConnectionPool() {}

    public static synchronized ConnectionPool getInstance()
    {
        if (instance == null)
        {
            ds = createHikariConnectionPool();
            instance = new ConnectionPool();
        }

        return instance;
    }

    public Connection getConnection() throws SQLException
    {
        return ds.getConnection();
    }

    public void close()
    {
        if (ds != null)
        {
            ds.close();
        }
    }

    private static HikariDataSource createHikariConnectionPool()
    {
        HikariConfig config = new HikariConfig();


        config.setJdbcUrl("jdbc:postgresql://164.92.243.232:5432/carport");

        config.setUsername("postgres");
        config.setPassword("Kingfisher17");

        return new HikariDataSource(config);
    }
}