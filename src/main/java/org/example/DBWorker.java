package org.example;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBWorker {
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String USERNAME = "root1";
    private static final String PASSWORD = "root";


    public static Connection getConnection() {
        Connection connection;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver(); // для старых версий Java
            DriverManager.registerDriver(driver);           // для старых версий Java
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection OK");
        } catch (SQLException e) {
            throw new RuntimeException("Отсуствует соединение с БД");
        }
        return connection;
    }
}
