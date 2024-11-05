package org.example.bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.bank.database.DatabaseR;
import org.example.bank.database.repository.IDB;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/lobby.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 666);
        stage.setTitle("Maslyak Bank");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        IDB dataBase = DatabaseR.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}