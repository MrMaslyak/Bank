package org.example.bank.registrationSystem.database;

import javafx.application.Platform;
import org.example.bank.registrationSystem.Registration;
import org.example.bank.registrationSystem.SystemR;
import org.example.bank.registrationSystem.database.repository.IDB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseR implements IDB {


    private static DatabaseR dataBase;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "LaLa27418182";
    private Registration register;

    public DatabaseR() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
            connection.close();

        } catch (Exception e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    public static DatabaseR getInstance() {
        if (dataBase == null) {
            dataBase = new DatabaseR();
        }
        return dataBase;
    }


    @Override
    public void addUser(String login, String password, String email){



        String query = "INSERT INTO bankUsers (login, password, email) VALUES (?, ?, ?) " +
                "ON CONFLICT (login) DO UPDATE SET password = EXCLUDED.password, email = EXCLUDED.email";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, email);

            statement.executeUpdate();
            System.out.println("Пользователь добавлен или обновлен успешно.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении пользователя.");
            e.printStackTrace();
        }
    }

    public boolean availableLogin(String login) {
        String query = "SELECT * FROM bankUsers WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Пользователь существует в базе.");
                Platform.runLater(() -> register.showError("User with this login already exists"));
                return true;
            } else {
                System.out.println("Пользователь с таким логином не найден.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Ошибка при поиске пользователя.");
            e.printStackTrace();
        }
        return false;
    }

    public void setRegistration(Registration registration) {
        this.register = registration;
    }

}
