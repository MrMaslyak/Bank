package org.example.bank.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseR {

    private static volatile DatabaseR instance;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseR.class);

    private DatabaseR() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            logger.info("Connected to the PostgreSQL server successfully.");
        } catch (Exception e) {
            logger.error("Connection failure during initialization.", e);
        }

    }

    public static DatabaseR getInstance() {
        if (instance == null) {
            synchronized (DatabaseR.class) {
                if (instance == null) {
                    instance = new DatabaseR();
                    logger.info("DatabaseR instance created.");
                }
            }
        }
        return instance;
    }

    public void addUser(String login, String password, String email) {
        String userQuery = "INSERT INTO bankUsers (login, password, email) VALUES (?, ?, ?) " +
                "ON CONFLICT (login) DO UPDATE SET password = EXCLUDED.password, email = EXCLUDED.email";
        String tokenQuery = "INSERT INTO bank_user_auth_token (user_id, token) VALUES (?, NULL) " +
                "ON CONFLICT (user_id) DO NOTHING";
        String balanceQuery = "INSERT INTO bank_user_balance (user_id, balance_usd) VALUES (?, 0) " +
                "ON CONFLICT (user_id) DO NOTHING";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement userStatement = connection.prepareStatement(userQuery);
             PreparedStatement tokenStatement = connection.prepareStatement(tokenQuery);
             PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery)) {

            userStatement.setString(1, login);
            userStatement.setString(2, password);
            userStatement.setString(3, email);
            userStatement.executeUpdate();

            int userId = getUserId(login);
            if (userId == -1) {
                logger.error("Failed to retrieve user_id for login '{}'.", login);
                return;
            }

            tokenStatement.setInt(1, userId);
            tokenStatement.executeUpdate();


            balanceStatement.setInt(1, userId);
            balanceStatement.executeUpdate();

            logger.info("User and related records added/updated successfully.");
        } catch (Exception e) {
            logger.error("Error during user addition or update.", e);
        }
    }



    public int getUserId(String login) {
        String query = "SELECT user_id FROM bankusers WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (Exception e) {
            logger.error("Error fetching user ID.", e);
        }
        return -1;
    }


    public boolean passBank(String login, String password) {
        String query = "SELECT * FROM bankUsers WHERE login = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();

            if (exists) {
                logger.info("User authenticated successfully.");
            } else {
                logger.warn("Invalid login or password.");
            }
            return exists;
        } catch (Exception e) {
            logger.error("Error during user authentication.", e);
            return false;
        }
    }

    public boolean availableLogin(String login) {
        return isFieldExists("SELECT 1 FROM bankUsers WHERE login = ?", login);
    }

    public boolean availableEmail(String email) {
        return isFieldExists("SELECT 1 FROM bankUsers WHERE email = ?", email);
    }

    private boolean isFieldExists(String query, String fieldValue) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, fieldValue);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();

            if (exists) {
                logger.warn("{} already exists in the database.", fieldValue);
            } else {
                logger.info("{} is available for use.", fieldValue);
            }
            return exists;
        } catch (Exception e) {
            logger.error("Error checking field existence.", e);
            return false;
        }
    }

}
