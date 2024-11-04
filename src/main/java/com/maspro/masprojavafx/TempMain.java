package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TempMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidInputException {

        //LOAD EXTENT
        try (FileInputStream file = new FileInputStream(ObjectPlus.EXTENT_FILENAME);
             ObjectInputStream ois = new ObjectInputStream(file)) {
            ObjectPlus.loadExtent(ois);
        } catch (ClassNotFoundException | FileNotFoundException e) {
            throw e;
        }
        //DATA TO ADD
//        ShopWorker manager = new ShopWorker("The", "Manager", Role.MANAGER, 3000.00, null);
//        ShopWorker technician = new ShopWorker("The", "Technician", Role.TECHNICIAN, 2500.00, "does everything at professional level");
//        new Customer("Adam", "Kowalski", "123456789", new Address("Podlaska", "Warszawa", "01-432"));
//        new Customer("Szymon", "Adamowski", "222333444", new Address("Podleśna", "Warszawa", "01-742"));
//        new Customer("Krzysztof", "Nowak", "666777888", new Address("Laskowa", "Warszawa", "01-126"));
//        new Model("Skoda", "Octavia");
//        new Model("Mazda", "MX-5");
//        new Car(ObjectPlus.getExtent(Customer.class).get(1),"WDMIATA", LocalDate.of(2023, 9, 12), ObjectPlus.getExtent(Model.class).get(1), Set.of(Tag.C_ROADSTER, Tag.C_SPORT_CAR), Set.of(DriveType.COMBUSTION), 1600, FuelType.GASOLINE, null, 1);
//        new Car(ObjectPlus.getExtent(Customer.class).get(0),"WD1234D", LocalDate.of(2023, 11, 12), ObjectPlus.getExtent(Model.class).get(0), Set.of(Tag.C_COUPE), Set.of(DriveType.COMBUSTION), 1400, FuelType.GASOLINE, null, 10);
//        new Service("Tire change", 120, new int[]{15, 30});
//        new Service("Oil change", 250, new int[]{20, 45});
//        new Appointment(List.of(ObjectPlus.getExtent(Service.class).get(0)), "WD1234D", List.of(ObjectPlus.getExtent(ShopWorker.class).get(1)), LocalDateTime.of(2023, 07, 24, 13, 15));

//        new Payment(PaymentType.CARD, ObjectPlus.getExtent(Appointment.class).get(0));
//        //SAVE EXTENT
        try (FileOutputStream file = new FileOutputStream(ObjectPlus.EXTENT_FILENAME);
             ObjectOutputStream oos = new ObjectOutputStream(file)){
            ObjectPlus.saveExtent(oos);
        }
    }
}
