package app;

import app.persistence.ConnectionPool;

import java.sql.Connection;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        ConnectionPool pool = ConnectionPool.getInstance();

        try (Connection conn = pool.getConnection())
        {
            System.out.println("URL = " + System.getenv("JDBC_URL"));
            System.out.println("USER=[" + System.getenv("JDBC_USER") + "]");
            System.out.println("PASSWORD = " + System.getenv("JDBC_PASSWORD"));
            System.out.println("Connected!");
        }
    }
}