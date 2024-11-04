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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseActionCustomersController implements Initializable { // allows to choose management option of customer

    @FXML
    private Button fxHome;

    @FXML
    private Button fxLogout;

    @FXML
    private Button fxViewCustomersInfo;
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
    void onViewCustomersInfoClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chooseViewCustomer_view.fxml"));
        root = loader.load();
        ChooseViewCustomerController chooseViewCustomerController = loader.getController();
        chooseViewCustomerController.passUser(user);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
