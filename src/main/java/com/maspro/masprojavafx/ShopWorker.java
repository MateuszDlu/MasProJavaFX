package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ShopWorker extends Person {
    private double payroll; // monthly payroll
    private List<Order> orders; // list of orders the worker participated in
    private List<Appointment> appointments; // technician's appointments
    private String specialization; // description of technician's specialization
    private Role role;

    public ShopWorker(String name, String surname, Role role, double payroll, String specialization) throws InvalidInputException {
        super(name, surname);
        setRole(role);
        setPayroll(payroll);
        setSpecialization(specialization);
    }

    public void setPayroll(double payroll) throws InvalidInputException {
        if (role == Role.MANAGER) {
            if (payroll < 1800) {
                throw new InvalidInputException("invalid payroll");
            }
        } else {
            if (payroll < 1500.00) {
                throw new InvalidInputException("invalid payroll");
            }
        }
        this.payroll = payroll;
    }

    public void setSpecialization(String specialization) throws InvalidInputException {
        if (role != Role.TECHNICIAN) {
            this.specialization = null;
        } else {
            if (specialization == null || specialization.isEmpty()) {
                throw new InvalidInputException("invalid specialization");
            }
            this.specialization = specialization;
        }
    }

    public void setRole(Role role) throws InvalidInputException {
        if (role == null) {
            throw new InvalidInputException("invalid role");
        }
        this.role = role;
    }

    public double getPayroll() {
        return payroll;
    }

    public List<Order> getOrders() {
        if (orders == null){
            return null;
        }
        return Collections.unmodifiableList(orders);
    }

    public List<Appointment> getAppointments() throws InvalidInputException {
        if (role != Role.TECHNICIAN) {
            throw new InvalidInputException("invalid role");
        }
        if (appointments == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(appointments);
    }

    public String getSpecialization() throws InvalidInputException {
        if (role != Role.TECHNICIAN) {
            throw new InvalidInputException("invalid role");
        }
        return specialization;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public double appointmentEstimation(Appointment appointment) {
        if (role == Role.MANAGER) {
            return appointment.getTotalCost() / appointment.getTotalTimeEstimate();
        } else {
            return appointment.getTotalTimeEstimate();
        }

    }

    protected void addOrder(Order order) throws InvalidInputException {
        if (order == null) {
            throw new InvalidInputException("invalid order");
        }
        if (orders == null) {
            orders = new ArrayList<>();
        }
        if (orders.contains(order)) {
            throw new InvalidInputException("invalid order");
        }
        orders.add(order);
    }

    public void addServicesToAppointment(Appointment appointment, List<Service> services) throws InvalidInputException {
        if (role != Role.TECHNICIAN) {
            throw new InvalidInputException("invalid role");
        }
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        if (services == null) {
            throw new InvalidInputException("invalid services");
        }
        if (!appointments.contains(appointment)) {
            throw new InvalidInputException("access denied");
        }
        for (Service s : services) {
            if (appointment.getServices().contains(s)) {
                services.remove(s);
            }
        }
        for (Service s : services) {
            appointment.addService(s);
        }
    }

    protected void removeAppointment(Appointment appointment) throws InvalidInputException {
        if (role != Role.TECHNICIAN) {
            throw new InvalidInputException("invalid role");
        }
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        if (!appointments.contains(appointment)) {
            throw new InvalidInputException("invalid appointment");
        }
        appointments.remove(appointment);
    }

    protected void addAppointment(Appointment appointment) throws InvalidInputException {
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }

    private void deleteShopWorker() throws InvalidInputException {
        if (orders != null) {
            for (Order o : orders) {
                o.removeOrder();
            }
        }
        if (appointments != null) {
            for (Appointment a : appointments) {
                a.removeTechnicianRelation(this);
            }
        }
        removeThisFromClassExtent();
    }

    protected void changeRoleToTechnician(ShopWorker manager, String specialization, double payroll) throws InvalidInputException {
        if (manager.getRole() == Role.MANAGER) {
            if (specialization == null || specialization.isEmpty()) {
                throw new InvalidInputException("invalid specialization");
            }
            manager.setRole(Role.TECHNICIAN);
            manager.setSpecialization(specialization);
            manager.setPayroll(payroll);
            appointments = new ArrayList<>();
        }
    }

    protected void changeRoleToManager(ShopWorker technician, double payroll) throws InvalidInputException {
        if (technician.getRole() == Role.TECHNICIAN) {
            technician.setRole(Role.MANAGER);
            specialization = null;
            technician.setPayroll(payroll);
            appointments = null;
        }
    }

    //USE CASES
    //manage customers
    public List<Customer> viewCustomers() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        return getExtent(Customer.class);
    }

    public Appointment viewCustomersAppointments(String phone, LocalDateTime appointmentDate) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (phone == null) {
            throw new InvalidInputException("invalid phone");
        }
        List<Customer> customerList = getExtent(Customer.class);
        AtomicReference<Customer> customer = new AtomicReference<>();
        customerList.forEach((c) -> {
            if (Objects.equals(c.getPhone(), phone)) {
                customer.set(c);
            }
        });
        List<Appointment> appointmentList = customer.get().getAppointments();
        AtomicReference<Appointment> appointment = new AtomicReference<>();
        appointmentList.forEach((a) -> {
            if (a.getAppointmentDate() == appointmentDate) {
                appointment.set(a);
            }
        });
        return appointment.getPlain();
    }

    public void newCustomer(String name, String surname, String phone, Address address, String email) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Customer(name, surname, phone, address, email);
    }

    public void newCustomer(String name, String surname, String phone, Address address) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Customer(name, surname, phone, address);
    }

    public void updateCustomer(Customer customer, String name, String surname, String phone, Address address, String email) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!customer.getName().equals(name)) {
            customer.setName(name);
        }
        if (!customer.getSurname().equals(surname)) {
            customer.setSurname(surname);
        }
        if (!customer.getPhone().equals(phone)) {
            customer.setPhone(phone);
        }
        if (!customer.getAddress().getCity().equals(address.getCity()) || !customer.getAddress().getPostal().equals(address.getPostal()) || !customer.getAddress().getStreet().equals(address.getStreet())) {
            customer.setAddress(address);
        }
        if (!customer.getEmail().equals(email)) {
            customer.setEmail(email);
        }
    }

    public void updateCustomer(Customer customer, String name, String surname, String phone, Address address) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!customer.getName().equals(name)) {
            customer.setName(name);
        }
        if (!customer.getSurname().equals(surname)) {
            customer.setSurname(surname);
        }
        if (!customer.getPhone().equals(phone)) {
            customer.setPhone(phone);
        }
        if (!customer.getAddress().getCity().equals(address.getCity()) || !customer.getAddress().getPostal().equals(address.getPostal()) || !customer.getAddress().getStreet().equals(address.getStreet())) {
            customer.setAddress(address);
        }
    }

    public void deleteCustomer(Customer customer, String phone) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (customer == null) {
            throw new InvalidInputException("invalid customer");
        }
        if (!Objects.equals(customer.getPhone(), phone)) {
            throw new InvalidInputException("validation failed");
        }
        customer.deleteCustomer();
    }

    //manage services
    public List<Service> viewServices() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        return getExtent(Service.class);
    }

    public void newService(String name, double price, int[] time) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Service(name, price, time);
    }

    public void updateService(Service service, String name, double price, int[] time) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!service.getName().equals(name)) {
            service.setName(name);
        }
        if (service.getPrice() != price) {
            service.setPrice(price);
        }
        if (service.getTime()[0] != time[0] || service.getTime()[1] != time[1]) {
            service.setTime(time);
        }
    }

    public void deleteService(Service service) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (service == null) {
            throw new InvalidInputException("invalid service");
        }
        service.deleteService();
    }

    //manage cars
    public List<Car> viewCars() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        return getExtent(Car.class);
    }

    public void newCar(Customer owner, String registration, LocalDate technicalExaminationExpirationDate, Model model, Set<Tag> tags, Set<DriveType> driveTypes, Integer engineCapacity, FuelType fuelType, Integer batteryCapacity, int storageCapacity) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Car(owner, registration, technicalExaminationExpirationDate, model, tags, driveTypes, engineCapacity, fuelType, batteryCapacity, storageCapacity);
    }

    public void updateCar(Car car, LocalDate technicalExaminationExpirationDate, Set<Tag> tags, Set<DriveType> driveTypes, Integer engineCapacity, FuelType fuelType, Integer batteryCapacity, int storageCapacity) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!car.getTechnicalExaminationExpirationDate().equals(technicalExaminationExpirationDate)) {
            car.setTechnicalExaminationExpirationDate(technicalExaminationExpirationDate);
        }
        if (!car.getTags().equals(tags)) {
            car.setTags(tags);
        }
        if (!car.getDriveTypes().equals(driveTypes)) {
            car.setDriveTypes(driveTypes);
        }
        if (!Objects.equals(car.getEngineCapacity(), engineCapacity)) {
            car.setEngineCapacity(engineCapacity);
        }
        if (!car.getFuelType().equals(fuelType)) {
            car.setFuelType(fuelType);
        }
        if (!Objects.equals(car.getBatteryCapacity(), batteryCapacity)) {
            car.setBatteryCapacity(batteryCapacity);
        }
        if (!Objects.equals(car.getBatteryCapacity(), batteryCapacity)) {
            car.setBatteryCapacity(batteryCapacity);
        }
        if (!Objects.equals(car.getStorageCapacity(), storageCapacity)) {
            car.setStorageCapacity(storageCapacity);
        }
    }

    public void deleteCar(Car car) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (car == null) {
            throw new InvalidInputException("invalid car");
        }
        car.deleteCar();
    }

    // manage motorcycles
    public List<Motorcycle> viewMotorcycles() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        return getExtent(Motorcycle.class);
    }

    public void newMotorcycle(Customer owner, String registration, LocalDate technicalExaminationExpirationDate, Model model, Set<Tag> tags, Set<DriveType> driveTypes, Integer engineCapacity, FuelType fuelType, Integer batteryCapacity, boolean hasStorage) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Motorcycle(owner, registration, technicalExaminationExpirationDate, model, tags, driveTypes, engineCapacity, fuelType, batteryCapacity, hasStorage);
    }

    public void updateMotorcycle(Motorcycle motorcycle, LocalDate technicalExaminationExpirationDate, Set<Tag> tags, Set<DriveType> driveTypes, Integer engineCapacity, FuelType fuelType, Integer batteryCapacity, boolean hasStorage) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!motorcycle.getTechnicalExaminationExpirationDate().equals(technicalExaminationExpirationDate)) {
            motorcycle.setTechnicalExaminationExpirationDate(technicalExaminationExpirationDate);
        }
        if (!motorcycle.getTags().equals(tags)) {
            motorcycle.setTags(tags);
        }
        if (!motorcycle.getDriveTypes().equals(driveTypes)) {
            motorcycle.setDriveTypes(driveTypes);
        }
        if (!Objects.equals(motorcycle.getEngineCapacity(), engineCapacity)) {
            motorcycle.setEngineCapacity(engineCapacity);
        }
        if (!motorcycle.getFuelType().equals(fuelType)) {
            motorcycle.setFuelType(fuelType);
        }
        if (!Objects.equals(motorcycle.getBatteryCapacity(), batteryCapacity)) {
            motorcycle.setBatteryCapacity(batteryCapacity);
        }
        if (!Objects.equals(motorcycle.getBatteryCapacity(), batteryCapacity)) {
            motorcycle.setBatteryCapacity(batteryCapacity);
        }
        if (!Objects.equals(motorcycle.HasStorage(), hasStorage)) {
            motorcycle.setHasStorage(hasStorage);
        }
    }

    public void deleteMotorcycle(Motorcycle motorcycle) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (motorcycle == null) {
            throw new InvalidInputException("invalid motorcycle");
        }
        motorcycle.deleteMotorcycle();
    }

    // add model
    public void addModel(String make, String model) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Model(make, model);
    }

    //view vehicles
    public List<Object> viewVehicles() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        return Vehicle.getListOfVehicles();
    }

    public List<Object> viewVehiclesNearExaminationExpirationDate() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        return Vehicle.getNearTechnicalExaminationDueList();
    }

    //create payment
    public void newPayment(PaymentType paymentType, Appointment appointment) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Payment(paymentType, appointment);
    }

    // add appointment, delete appointment
    public void newAppointment(List<Service> services, String registration, List<ShopWorker> technicians, LocalDateTime appointmentDate) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Appointment(services, registration, technicians, appointmentDate);
    }

    public void deleteAppointment(Appointment appointment) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        appointment.deleteAppointment();
    }

    //make order and change status
    public void newOrder(ShopWorker manager, ShopWorker technician) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Order(manager, technician);
    }

    public void changeOrderStatus(Order order, Status status) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (order != null && order.getManager() == this && status != null) {
            order.setStatus(status);
        } else {
            throw new InvalidInputException("invalid status change");
        }
    }

    //add items to order
    public void addItemToOrder(Order order, Item item, int quantity) throws InvalidInputException {
        if (role == Role.TECHNICIAN && order.getTechnician() != this) {
            throw new InvalidInputException("invalid access");
        }
        new OrderItem(order, item, quantity);
    }

    //manage items
    public List<Item> viewItems() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        return getExtent(Item.class);
    }

    public void newItem(String name, int stock) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new Item(name, stock);
    }

    public void updateItem(Item item, String name, int stock) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!item.getName().equals(name)) {
            item.setName(name);
        }
        if (item.getStock() != stock) {
            item.setStock(stock);
        }
    }

    public void deleteItem(Item item) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (item == null) {
            throw new InvalidInputException("invalid item");
        }
        item.deleteItem();
    }

    //manage managers
    public List<ShopWorker> viewManagers() throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        List<ShopWorker> shopWorkers = getExtent(ShopWorker.class);
        List<ShopWorker> managers = new ArrayList<>();
        shopWorkers.forEach((shopWorker -> {
            if (shopWorker.getRole() == Role.MANAGER) {
                managers.add(shopWorker);
            }
        }));
        return managers;
    }

    public void newManager(String name, String surname, double payroll) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new ShopWorker(name, surname, Role.MANAGER, payroll, null);
    }

    public void updateManager(ShopWorker manager, String name, String surname, double payroll) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!manager.getName().equals(name)) {
            manager.setName(name);
        }
        if (!manager.getSurname().equals(surname)) {
            manager.setSurname(surname);
        }
        if (manager.getPayroll() != payroll) {
            manager.setPayroll(payroll);
        }
    }

    public void deleteManager(ShopWorker manger) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (manger == null) {
            throw new InvalidInputException("invalid manager");
        }
        if (manger.getRole() != Role.MANAGER) {
            throw new InvalidInputException("invalid manager");
        }
        manger.deleteShopWorker();
    }

    public void changeManagerToTechnician(ShopWorker manager, String specialization, double payroll) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        changeRoleToTechnician(manager, specialization, payroll);
    }

    public void newTechnician(String name, String surname, double payroll, String specialization) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        new ShopWorker(name, surname, Role.TECHNICIAN, payroll, specialization);
    }

    public void updateTechnician(ShopWorker technician, String name, String surname, double payroll, String specialization) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (!technician.getName().equals(name)) {
            technician.setName(name);
        }
        if (!technician.getSurname().equals(surname)) {
            technician.setSurname(surname);
        }
        if (technician.getPayroll() != payroll) {
            technician.setPayroll(payroll);
        }
        if (!technician.getSpecialization().equals(specialization)) {
            technician.setSpecialization(specialization);
        }
    }

    public void deleteTechnician(ShopWorker technician) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        if (technician == null) {
            throw new InvalidInputException("invalid technician");
        }
        if (technician.getRole() != Role.TECHNICIAN) {
            throw new InvalidInputException("invalid technician");
        }
        technician.deleteShopWorker();
    }

    public void changeTechnicianToManager(ShopWorker technician, double payroll) throws InvalidInputException {
        if (role != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        changeRoleToManager(technician, payroll);
    }
    //TODO rest of use case from ADD APPOINTMENT
}
