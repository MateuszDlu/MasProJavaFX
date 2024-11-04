package com.maspro.masprojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ShopManagementApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException { // starts the application and loads the state of the system from file
        FXMLLoader fxmlLoader = new FXMLLoader(ShopManagementApplication.class.getResource("guest_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        try (FileInputStream file = new FileInputStream(ObjectPlus.EXTENT_FILENAME);
             ObjectInputStream ois = new ObjectInputStream(file)) {
            ObjectPlus.loadExtent(ois);
        } catch (ClassNotFoundException e) {
            throw e;
        }
        stage.setTitle("TireX");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}