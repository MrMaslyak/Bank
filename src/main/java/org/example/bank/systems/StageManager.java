package org.example.bank.systems;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.IOException;

public class StageManager {

    private static FXMLLoader currentLoader;

    public static void switchScene(Button currentButton, String fxmlPath) {
        try {
            currentLoader = new FXMLLoader(StageManager.class.getResource(fxmlPath));
            Parent root = currentLoader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            currentButton.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            Logger logger = org.slf4j.LoggerFactory.getLogger(StageManager.class);
            logger.error("Ошибка при смене сцены: " + fxmlPath, e);
        }
    }
    public static void switchScene(Button currentButton, Parent root) {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            currentButton.getScene().getWindow().hide();
            stage.show();
        } catch (Exception e) {
            Logger logger = org.slf4j.LoggerFactory.getLogger(StageManager.class);
            logger.error("Ошибка при смене сцены", e);
        }
    }



    public static void switchScene(String fxmlPath) {
        try {
            currentLoader = new FXMLLoader(StageManager.class.getResource(fxmlPath));
            Parent root = currentLoader.load();

            Stage currentStage = (Stage) Stage.getWindows().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Нет активного окна"));

            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            Logger logger = org.slf4j.LoggerFactory.getLogger(StageManager.class);
            logger.error("Ошибка при смене сцены: " + fxmlPath, e);
        }
    }


}
