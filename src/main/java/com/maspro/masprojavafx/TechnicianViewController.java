package com.maspro.masprojavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TechnicianViewController { // placeholder for technicians actions per documentation no GUI for those options
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ShopWorker user;

    protected void passUser(ShopWorker user) {
        this.user = user;
    }

    @FXML
    private Button fxHome;

    @FXML
    private Button fxLogout;

    @FXML
    void onLogoutClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("guest_view.fxml"));
        root = loader.load();
        loader.setController(new GuestController());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
