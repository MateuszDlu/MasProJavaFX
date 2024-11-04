package com.maspro.masprojavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class LoginController { // allows user to login and depending on login name the correct user will be determined
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ShopWorker user;
    @FXML
    private Button fxConfirmLoginButton;

    @FXML
    private Button fxHome;

    @FXML
    private Button fxLogin;

    @FXML
    private TextField fxLoginText;

    @FXML
    private PasswordField fxPasswordText;

    @FXML
    private Text fxInvalidCredentials;

    @FXML
    void onConfirmLoginClicked(ActionEvent event) throws IOException, ClassNotFoundException {

        String login = fxLoginText.getText();
        String password = fxPasswordText.getText();
        ObjectInputStream oosLogins = new ObjectInputStream(new FileInputStream("src/main/java/com/maspro/masprojavafx/logins.dat"));
        ObjectInputStream oosPasswords = new ObjectInputStream(new FileInputStream("src/main/java/com/maspro/masprojavafx/passwords.dat"));
        List<ShopWorker> shopWorkers = ObjectPlus.getExtent(ShopWorker.class);
        List<String> logins = (List<String>) oosLogins.readObject();
        List<String> passwords = (List<String>) oosPasswords.readObject();
        if (logins.contains(login)) {
            if (Objects.equals(passwords.get(logins.indexOf(login)), password)) {
                for (ShopWorker s : shopWorkers) {
                    if (login.toLowerCase().equals(s.getSurname().toLowerCase())) {
                        if (s.getRole() == Role.MANAGER) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("manager_view.fxml"));
                            root = loader.load();
                            ManagerViewController managerViewController = loader.getController();
                            managerViewController.passUser(s);
                        } else {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("technician_view.fxml"));
                            root = loader.load();
                            TechnicianViewController technicianViewController = loader.getController();
                            technicianViewController.passUser(s);
                        }
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                }
            } else {
                fxInvalidCredentials.setVisible(true);
            }
        } else {
            fxInvalidCredentials.setVisible(true);
        }
    }

    @FXML
    void onHomeClicked_guest(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("guest_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onLoginClicked(ActionEvent event) {

    }

}
