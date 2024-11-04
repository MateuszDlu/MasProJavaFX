package com.maspro.masprojavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerViewController implements Initializable { // full view of managers possibilities in the system but only working options are Add appointment and Manage customers per documentation
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ShopWorker user;

    protected void passUser(ShopWorker user) {
        this.user = user;
    }

    @FXML
    private Button fxAddAppointmentBtn;

    @FXML
    private Button fxHome;

    @FXML
    private Button fxLogout;

    @FXML
    private Button fxManageCustomersBtn;

    @FXML
    void onAddAppointmentClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addAppointment_view.fxml"));
        root = loader.load();
        AddAppointmentController addAppointmentController = loader.getController();
        addAppointmentController.passUser(user);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onLogoutClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("guest_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onManageCustomersClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chooseActionCustomer_view.fxml"));
        root = loader.load();
        ChooseActionCustomersController chooseActionCustomersController = loader.getController();
        chooseActionCustomersController.passUser(user);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
