package org.example;

import java.sql.*;

public class Testconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String USERNAME = "root1";
    public static final String PASSWORD = "root";

    public static void main(String[] args) {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            System.err.println("Ошибка в установке соединения с БД!");
        }
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();)
        {
            /// универсальная команда для вставки и получения данных
            statement.execute("INSERT INTO users (name, age, email) values ('Jim', 22, 'jim@mail.ru')");
            /// запросы Insert/Update/Delete. Метод statement.executeUpdate возвращает количество измененных строк
            int res = statement.executeUpdate("UPDATE users set name = 'Updated' where id = 1");
            System.out.println(res);
            /// пакетный запрос на нескоько операций:
            statement.addBatch("INSERT INTO users (name, age, email) values ('Batch1', 1, 'non')");
            statement.addBatch("INSERT INTO users (name, age, email) values ('Batch2', 1, 'non')");
            statement.addBatch("INSERT INTO users (name, age, email) values ('Batch3', 1, 'non')");
            // для выполнения пакетного запроса:
            statement.executeBatch();
            // для очистки Batch (партии):
            statement.clearBatch();
            // получаем соедениение с БД для дальнейшей работы (если оно разорвано):
            statement.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
