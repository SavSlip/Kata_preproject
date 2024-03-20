package org.example;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class App {
    private static Connection connection = DBWorker.getConnection();

    public static void insertUser() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(MyQuery.INSERT.command)) {
            preparedStatement.setString(1, "Pokupatel");
            preparedStatement.setString(2, "Pokupatel");
            preparedStatement.setInt(3, 100);
            if (preparedStatement.executeUpdate() == 1) {
                System.out.println("Пользователь успешно добавлен");
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception occurred: " + e.getMessage(), e);
        }
    }

    public static void pokupka(int cena, String prodavetc, String pokupatel) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET age = age + ? WHERE name = ?");
             PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE users SET age = age - ? WHERE name = ?");
             PreparedStatement proverka = connection.prepareStatement("SELECT age from users where name = ?")) {
            proverka.setString(1, pokupatel);
            ResultSet proverkaO = proverka.executeQuery();
            proverkaO.next();
            if (proverkaO.getInt("age") >= cena) {
                connection.setAutoCommit(false);
                preparedStatement.setInt(1, cena);
                preparedStatement.setString(2, prodavetc);
                preparedStatement2.setInt(1, cena);
                preparedStatement2.setString(2, pokupatel);;
                if (preparedStatement2.executeUpdate() == preparedStatement.executeUpdate()) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            } else {
                System.err.println("У продавца не хватает денег на товар");
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception occurred: " + e.getMessage(), e);
        }
    }

    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(MyQuery.SELECT.command);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("Id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void main(String[] args) throws SQLException {
        //    insertUser();
        //    getAllUsers().forEach(System.out::println);
        pokupka(10, "Prodavetc", "Pokupatel");
    }
}


enum MyQuery {
    DELETE("DELETE  from users where id = ?"),
    INSERT("INSERT into users (name, lastname, age) values (?,?,?)"),
    INSERT2("INSERT into users (name, last_name, age) values (?,?,?)"),
    SELECT("SELECT * FROM users");

    public final String command;

    MyQuery(String s) {
        command = s;
    }
}
