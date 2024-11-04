package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ViewAppointmentsCustomerController implements Initializable { // allows manager to view specified customer's appointments after choosing phone number of customer and from customer's appointments the date of an appointment

    @FXML
    private ComboBox<Appointment> fxAppointmentDate;

    @FXML
    private TextArea fxAppointmentTextField;

    @FXML
    private ComboBox<Customer> fxCustomerPhone;

    @FXML
    private Text fxNoAppointmentsError;

    @FXML
    private Text fxInvalidCustomer;

    @FXML
    private Button fxHome;

    @FXML
    private Button fxLogout;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ShopWorker user;
    private Customer selectedCustomer;

    protected void passUser(ShopWorker user) {
        this.user = user;
        loadData();
    }

    // populating customer list with corresponding phone number
    private static class CustomerListCell extends ListCell<Customer>{
        @Override
        public void updateItem(Customer item, boolean empty){
            super.updateItem(item, empty);
            if (item != null){
                setText(item.getPhone());
            }else {
                setText(null);
            }
        }
    }
    // populating appointment list with corresponding dates
    private static class AppointmentListCell extends ListCell<Appointment>{
        @Override
        public void updateItem(Appointment item, boolean empty){
            super.updateItem(item, empty);
            if (item != null){
                setText(item.getAppointmentDate().toString());
            }else {
                setText(null);
            }
        }
    }

    private void loadData() { //loads customers to choose from in combobox
        List<Customer> customers = ObjectPlus.getExtent(Customer.class);
        for (Customer c : customers) {
            fxCustomerPhone.getItems().add(c);
        }
        fxCustomerPhone.setCellFactory(listCell -> new CustomerListCell());
        fxCustomerPhone.setButtonCell(new CustomerListCell());
    }

    @FXML
    void onPhone(ActionEvent event) { // when phone is changed the system checks if such customer exists and if then fills date combo box with dates of customer's appointments
        fxInvalidCustomer.setVisible(false);
        fxNoAppointmentsError.setVisible(false);
        fxAppointmentDate.setDisable(true);
        fxAppointmentDate.getItems().setAll();
        fxAppointmentTextField.clear();

        selectedCustomer = fxCustomerPhone.getValue();
        boolean flag = false;
        if (selectedCustomer.getAppointments() == null){
            flag = true;
        }else if (selectedCustomer.getAppointments().isEmpty()){
            flag = true;
        }

        if (flag) {
            selectedCustomer = null;
            fxAppointmentDate.setDisable(true);
            fxNoAppointmentsError.setVisible(true);
        } else {
            for (Appointment a : selectedCustomer.getAppointments()) {
                fxAppointmentDate.getItems().add(a);
            }
            fxAppointmentDate.setCellFactory(listCell -> new AppointmentListCell());
            fxAppointmentDate.setButtonCell(new AppointmentListCell());
            fxAppointmentDate.setDisable(false);
        }

    }

    @FXML
    void onDateSet(ActionEvent event) throws InvalidInputException { // date can be selected only after customer is set, after choosing date there will be visible all necessary information about the appointment
        try {
            Appointment appointment = user.viewCustomersAppointments(selectedCustomer.getPhone(), fxAppointmentDate.getValue().getAppointmentDate());
            //Appointment appointment = fxAppointmentDate.getValue();
            if (appointment == null) {
                fxAppointmentTextField.setText("Appointment not found");
            } else {
                String customerInfo = "Customer: " + appointment.getCustomer().getName() + " " + appointment.getCustomer().getSurname() + "\nTel:" + appointment.getCustomer().getPhone();
                String motorcycleInfo = null;
                String carInfo = null;
                if (appointment.getCar() != null) {
                    carInfo = "Car: " + appointment.getCar().getRegistration() + "\n" + appointment.getCar().getModel().getMake() + " " + appointment.getCar().getModel().getModel();
                } else if (appointment.getMotorcycle() != null) {
                    motorcycleInfo = "Motorcycle: " + appointment.getMotorcycle().getRegistration() + "\n" + appointment.getMotorcycle().getModel().getMake() + " " + appointment.getMotorcycle().getModel().getModel();
                }
                String techniciansInfo = "Technicians: ";
                for (ShopWorker t : appointment.getTechnicians()) {
                    techniciansInfo += "\n" + t.getName() + " " + t.getSurname();
                }
                String servicesInfo = "Services: ";
                for (Service s : appointment.getServices()) {
                    servicesInfo += "\n" + s.getName() + " " + s.getPrice();
                }
                String appointmentDateInfo;
                if (String.valueOf(appointment.getAppointmentDate().getMonthValue()).length() == 1) {
                    appointmentDateInfo = "Date: " + appointment.getAppointmentDate().getDayOfMonth() + ".0" + appointment.getAppointmentDate().getMonthValue() + "." + appointment.getAppointmentDate().getYear() + "\t" + appointment.getAppointmentDate().getHour() + ":" + appointment.getAppointmentDate().getMinute();
                } else {
                    appointmentDateInfo = "Date: " + appointment.getAppointmentDate().getDayOfMonth() + "." + appointment.getAppointmentDate().getMonthValue() + "." + appointment.getAppointmentDate().getYear() + "\t" + appointment.getAppointmentDate().getHour() + ":" + appointment.getAppointmentDate().getMinute();
                }
                String paymentInfo;
                if (appointment.getPayment() == null) {
                    paymentInfo = "Payment: N/A";
                } else {
                    paymentInfo = "Payment: " + appointment.getPayment().getTotal() + " Status: " + appointment.getPayment().getType();
                }
                if (carInfo != null) {
                    fxAppointmentTextField.setText(appointmentDateInfo + "\n\n" + customerInfo + "\n\n" + carInfo + "\n\n" + techniciansInfo + "\n\n" + servicesInfo + "\n\n" + paymentInfo);
                } else {
                    fxAppointmentTextField.setText(appointmentDateInfo + "\n\n" + customerInfo + "\n\n" + motorcycleInfo + "\n\n" + techniciansInfo + "\n\n" + servicesInfo + "\n\n" + paymentInfo);
                }
            }
        } catch (Exception e) {
//            fxNoAppointmentsError.setVisible(true);
        }

    }

    @FXML
    void onHomeClicked(ActionEvent event) throws IOException { // returns to manager home view
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
    void onLogoutClicked(ActionEvent event) throws IOException { // logs out and saves the extent
        FXMLLoader loader = new FXMLLoader(getClass().getResource("guest_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
