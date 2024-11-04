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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable { // allows to add a new appointment in series of steps to determine next step's contents

    @FXML
    private Button fxAddVehicle;

    @FXML
    private Button fxConfirmBtn;

    @FXML
    private Button fxConfirmDateBtn;

    @FXML
    private Button fxConfirmServicesBtn;

    @FXML
    private DatePicker fxDateInput;

    @FXML
    private Button fxHome;

    @FXML
    private TextField fxHourInput;

    @FXML
    private ComboBox<String> fxServiceBox;

    @FXML
    private Button fxLogout;

    @FXML
    private TextField fxMinuteInput;

    @FXML
    private TextField fxRegistration;

    @FXML
    private ListView<String> fxServicesChosen;

    @FXML
    private ComboBox<ShopWorker> fxTechnicianBox;

    @FXML
    private Text fxMaxTechniciansError;

    @FXML
    private Text fxFormError;

    @FXML
    private Text fxNoTechniciansError;

    @FXML
    private Text fxNoServicesChosen;

    @FXML
    private ListView<String> fxTechniciansChosen;

    @FXML
    private Text fxDateError;

    @FXML
    private Text fxVehicleError;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ShopWorker user;
    private List<Service> services = new ArrayList<>();
    private List<ShopWorker> technicians = new ArrayList<>();
    private List<ShopWorker> finalTechnicians = new ArrayList<>();
    List<String> registrations = new ArrayList<>();
    List<Car> cars = new ArrayList<>();
    List<Motorcycle> motorcycles = new ArrayList<>();
    List<Service> finalServices = new ArrayList<>();
    LocalDateTime finalAppointmentDate;

    protected void passUser(ShopWorker user) {
        this.user = user;
        loadData();
    }

    private static class TechnicianListCell extends ListCell<ShopWorker>{
        @Override
        public void updateItem(ShopWorker item, boolean empty){
            super.updateItem(item, empty);
            if (item != null){
                setText(item.getName() + " " + item.getSurname());
            }else {
                setText(null);
            }
        }
    }

    private boolean checkDateFields(){
        if (fxDateInput.getValue() == null || fxHourInput.getText() == null || fxMinuteInput.getText() == null) {
            return true;
        } else if (fxDateInput.getValue().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
            return true;
        } else if (!fxHourInput.getText().matches("[0-9]+") || !fxMinuteInput.getText().matches("[0-9]+")) {
            return true;
        } else if (Integer.parseInt(fxHourInput.getText()) > 23 || Integer.parseInt(fxHourInput.getText()) < 0 || Integer.parseInt(fxMinuteInput.getText()) > 59 || Integer.parseInt(fxMinuteInput.getText()) < 0) {
            return true;
        }
        return false;
    }

    private void loadData() { // loads data for swift use on runtime
        services = ObjectPlus.getExtent(Service.class);
        for (ShopWorker s : ObjectPlus.getExtent(ShopWorker.class)) {
            if (s.getRole() == Role.TECHNICIAN) {
                technicians.add(s);
            }
        }
        for (Service s : services) {
            fxServiceBox.getItems().add(s.getName());
        }
        cars = ObjectPlus.getExtent(Car.class);
        motorcycles = ObjectPlus.getExtent(Motorcycle.class);
        for (Vehicle vehicle : cars) {
            registrations.add(vehicle.getRegistration());
        }
        for (Vehicle vehicle : motorcycles) {
            registrations.add(vehicle.getRegistration());
        }
    }

    @FXML
    void onConfirmClicked(ActionEvent event) { // checks if all data is correct and forwards user to manager home screen, if data is not correct form won't be submitted and error will appear
        try {
            if (!registrations.contains(fxRegistration.getText()) || fxRegistration.getText().isEmpty()) {
                fxVehicleError.setVisible(true);
                throw new InvalidInputException("invalid form");
            }
            if (fxServicesChosen.getItems().isEmpty()){
                fxNoServicesChosen.setVisible(true);
                throw new InvalidInputException("invalid form");
            }else if (fxTechniciansChosen.getItems().isEmpty()) {
                fxNoTechniciansError.setVisible(true);
                throw new InvalidInputException("invalid form");
            }
            for (String surname : fxTechniciansChosen.getItems()) {
                for (ShopWorker technician : technicians) {
                    if (Objects.equals(technician.getSurname(), surname)) {
                        finalTechnicians.add(technician);
                    }
                }
            }
            user.newAppointment(finalServices, fxRegistration.getText(), finalTechnicians, finalAppointmentDate);
            try (FileOutputStream file = new FileOutputStream(ObjectPlus.EXTENT_FILENAME);
                 ObjectOutputStream oos = new ObjectOutputStream(file)) {
                 ObjectPlus.saveExtent(oos);
            }
            onHomeClicked(event);
        } catch (Exception e) {
            fxFormError.setVisible(true);
        }
    }

    @FXML
    void onConfirmServicesClicked(ActionEvent event) { // confirms and locks services adding. Necessary for appointment time estimation. Unlocks date inputs
        if (!fxServicesChosen.getItems().isEmpty()) {
            fxFormError.setVisible(false);
            fxServiceBox.setDisable(true);
            fxDateInput.setDisable(false);
            fxHourInput.setDisable(false);
            fxMinuteInput.setDisable(false);
            fxNoServicesChosen.setVisible(false);
            for (String name : fxServicesChosen.getItems()) {
                services.forEach((service -> {
                    if (Objects.equals(service.getName(), name)) {
                        finalServices.add(service);
                    }
                }));
            }
        }else{
            fxNoServicesChosen.setVisible(true);
        }
    }

    @FXML
    void onConfirmDateClicked(ActionEvent event){ // confirms all data inputted in date inputs and knowing the information about the date and length of the appointment populates technicians combobox with free technicians
        fxFormError.setVisible(false);
        fxTechnicianBox.getItems().setAll();
        fxTechniciansChosen.getItems().setAll();
        fxTechnicianBox.setDisable(true);
        fxNoTechniciansError.setVisible(false);
        fxDateError.setVisible(false);
        boolean flag = checkDateFields();
        boolean tFlag = false;
        if (!flag){
            finalAppointmentDate = LocalDateTime.of(fxDateInput.getValue().getYear(), fxDateInput.getValue().getMonthValue(), fxDateInput.getValue().getDayOfMonth(), Integer.parseInt(fxHourInput.getText()), Integer.parseInt(fxMinuteInput.getText()));
            int pessimisticTime = 0;
            for (Service s : finalServices) {
                pessimisticTime += s.getTime()[1];
            }
            for (ShopWorker technician : technicians) {
                try {
                    for (Appointment a : technician.getAppointments()) {
                        if (a.getAppointmentState() == AppointmentState.COMPLETED) {
                            continue;
                        } // if completed
                        if (a.getAppointmentPessimisticEnd().isBefore(finalAppointmentDate)) {
                            continue;
                        } // end time of checked appointment is before new appointment
                        if (a.getAppointmentDate().isAfter(finalAppointmentDate.plusMinutes(pessimisticTime))) {
                            continue;
                        } // end time of new appointment is before checked appointment
                        if (a.getAppointmentDate().equals(finalAppointmentDate)){
                            tFlag = true;
                            continue;
                        } // new and checked start at the same time
                        if (a.getAppointmentDate().isBefore(finalAppointmentDate) && a.getAppointmentPessimisticEnd().isAfter(finalAppointmentDate)) {
                            tFlag = true;
                            continue;
                        } // checked appointment starts before new but ends after start of new
                        if (a.getAppointmentDate().isBefore(finalAppointmentDate.plusMinutes(pessimisticTime)) && a.getAppointmentPessimisticEnd().isAfter(finalAppointmentDate.plusMinutes(pessimisticTime))) {
                            tFlag = true;
                            continue;
                        } // checked appointment completely overlaps new
                        if (finalAppointmentDate.isBefore(a.getAppointmentDate()) && finalAppointmentDate.plusMinutes(pessimisticTime).isAfter(a.getAppointmentDate())) {
                            tFlag = true;
                        } // new starts before checked appointment but ends after checked starts
                    }
                }catch (InvalidInputException e){
                    continue;
                }
                if (!tFlag){ // TODO fill properly
                    fxTechnicianBox.getItems().add(technician);
                    fxTechnicianBox.setCellFactory(listCell -> new TechnicianListCell());
                    fxTechnicianBox.setButtonCell(new TechnicianListCell());
                    //fxTechnicianBox.getItems().add(technician.getSurname()); // after checking all possibilities adds technician to list
                }
            }
        }

        if (flag) {
            fxTechnicianBox.getItems().setAll();
            fxDateError.setVisible(true);
        } else {
            fxDateError.setVisible(false);
            fxTechnicianBox.setDisable(false);
        }
        if (fxTechnicianBox.getItems().isEmpty()){
            fxNoTechniciansError.setVisible(true);
            fxTechnicianBox.setDisable(true);
        }

    }
    @FXML
    void onDateChange(MouseEvent event) {
        fxFormError.setVisible(false);
        fxTechnicianBox.getItems().setAll();
        fxTechniciansChosen.getItems().setAll();
        fxTechnicianBox.setDisable(true);
        fxNoTechniciansError.setVisible(false);
        fxDateError.setVisible(false);
    }
    @FXML
    void onHourChange(MouseEvent event) {
        fxFormError.setVisible(false);
        fxTechnicianBox.getItems().setAll();
        fxTechniciansChosen.getItems().setAll();
        fxTechnicianBox.setDisable(true);
        fxNoTechniciansError.setVisible(false);
        fxDateError.setVisible(false);
    }
    @FXML
    void onMinuteChange(MouseEvent event) {
        fxFormError.setVisible(false);
        fxTechnicianBox.getItems().setAll();
        fxTechniciansChosen.getItems().setAll();
        fxTechnicianBox.setDisable(true);
        fxNoTechniciansError.setVisible(false);
        fxDateError.setVisible(false);
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
    void onRegistrationInputted(ActionEvent event) { // checks if registration belongs to any vehicle in the system and if not displays an error
        fxFormError.setVisible(false);
        if (!registrations.contains(fxRegistration.getText())) {
            fxVehicleError.setVisible(true);
        } else {
            fxVehicleError.setVisible(false);
        }
    }

    @FXML
    void onServiceChosen(ActionEvent event) { // adds chosen service to list
        if (!fxServicesChosen.getItems().contains(fxServiceBox.getValue())) {
            fxServicesChosen.getItems().add(fxServiceBox.getValue());
        }
    }

    @FXML
    void onTechnicianChosen(ActionEvent event) { // adds chosen technician to list
        if (fxTechniciansChosen.getItems().size() == 4) {
            fxMaxTechniciansError.setVisible(true);
        } else if (!fxTechniciansChosen.getItems().contains(fxTechnicianBox.getValue().getSurname())) {
            fxTechniciansChosen.getItems().add(fxTechnicianBox.getValue().getSurname());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
