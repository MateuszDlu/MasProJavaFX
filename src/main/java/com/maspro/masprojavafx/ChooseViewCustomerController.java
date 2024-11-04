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

public class ChooseViewCustomerController implements Initializable { // allows to choose what about customer should be displayed

    @FXML
    private Button fxHome;

    @FXML
    private Button fxLogout;

    @FXML
    private Button fxViewCustomerAppointmentsBtn;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ShopWorker user;

    protected void passUser(ShopWorker user) {
        this.user = user;
    }

    @FXML
    void onHomeClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager_view.fxml"));
        root = loader.load();
        ManagerViewController managerViewController = loader.getController();
        managerViewController.passUser(user);
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
    void onViewCustomerAppointmentsClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewAppointmentsCustomer_view.fxml"));
        root = loader.load();
        ViewAppointmentsCustomerController viewAppointmentsCustomerController = loader.getController();
        viewAppointmentsCustomerController.passUser(user);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
