package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TestMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidInputException {
        //LOAD
        try (FileInputStream file = new FileInputStream(ObjectPlus.EXTENT_FILENAME);
             ObjectInputStream ois = new ObjectInputStream(file)) {
            ObjectPlus.loadExtent(ois);
        } catch (ClassNotFoundException | IOException e) {
            throw e;
        }

        ShopWorker manager = ObjectPlus.getExtent(ShopWorker.class).get(0);
        if (manager.getRole() == Role.MANAGER){
            System.out.println("manager");
        }
        System.out.println("===============================");
        Address address = null;
        for (Customer c : manager.viewCustomers()){
            System.out.println(c.getName() + c.getSurname() + c.getPhone() + c.getAddress() + c.getEmail());
            address = c.getAddress();
        }
        System.out.println("===============================");
        manager.updateCustomer(ObjectPlus.getExtent(Customer.class).get(2), "Krzysztof", "Nowak", "666666666", address);
        System.out.println(ObjectPlus.getExtent(Customer.class).get(2).getName() + ObjectPlus.getExtent(Customer.class).get(2).getSurname() + ObjectPlus.getExtent(Customer.class).get(2).getPhone() + ObjectPlus.getExtent(Customer.class).get(2).getAddress() + ObjectPlus.getExtent(Customer.class).get(2).getEmail());
        System.out.println("===============================");
        manager.deleteCustomer(ObjectPlus.getExtent(Customer.class).get(2), "666666666");
        for (Customer c : manager.viewCustomers()){
            System.out.println(c.getName() + c.getSurname() + c.getPhone() + c.getAddress() + c.getEmail());
        }
        System.out.println("===============================");
        System.out.println(manager.viewServices());
        System.out.println("===============================");
        manager.newService("TestService", 222.00, new int[]{2,3});
        System.out.println(manager.viewServices().get(2).getName());
        System.out.println("===============================");
        manager.updateService(ObjectPlus.getExtent(Service.class).get(2), "Updated", 222.00, new int[]{2,3});
        System.out.println(manager.viewServices().get(2).getName());
        System.out.println("===============================");
        manager.deleteService(ObjectPlus.getExtent(Service.class).get(2));
        System.out.println(manager.viewServices());
        System.out.println("===============================");
        System.out.println(manager.viewVehicles());
        System.out.println("===============================");
        System.out.println(manager.viewVehiclesNearExaminationExpirationDate());
        System.out.println("===============================");
        System.out.println("Appointments: " + ObjectPlus.getExtent(Appointment.class));
        System.out.println("Technicians Appointments: " + ObjectPlus.getExtent(ShopWorker.class).get(1).getAppointments());
        manager.deleteAppointment(ObjectPlus.getExtent(Appointment.class).get(1));
        System.out.println("Appointments: " + ObjectPlus.getExtent(Appointment.class));
        System.out.println("Technicians Appointments: " + ObjectPlus.getExtent(ShopWorker.class).get(1).getAppointments());
        System.out.println("===============================");
        System.out.println(manager.viewManagers());
        manager.changeTechnicianToManager(ObjectPlus.getExtent(ShopWorker.class).get(1), 5000);
        System.out.println(manager.viewManagers());

    }
}
