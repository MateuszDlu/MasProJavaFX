package com.maspro.masprojavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GuestController { // Operates login button and displays information on how to use the system

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button fxHome;

    @FXML
    private Button fxLogin;

    @FXML
    void onLoginClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login_view.fxml"));
        root = loader.load();

        loader.setController(new LoginController());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
