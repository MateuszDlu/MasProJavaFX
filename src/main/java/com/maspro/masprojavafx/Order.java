package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order extends ObjectPlus implements Serializable {
    private ShopWorker manager; // approving and/or ordering
    private ShopWorker technician; // ordering
    private Status status; // status of the order
    private List<OrderItem> orderItems; // collection of items and their quantity

    public Order(ShopWorker manager, ShopWorker technician) throws InvalidInputException {
        super();
        try {
            setManager(manager);
            setTechnician(technician);
            setStatus(Status.AWAITING_APPROVAL);
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }
    }

    public Status getStatus() {
        return status;
    }

    protected void setStatus(Status status) throws InvalidInputException {
        if (status != null) {
            this.status = status;
        } else throw new InvalidInputException("invalid status");
    }

    private boolean checkIfEditable() {
        if (status.equals(Status.AWAITING_APPROVAL)) {
            return true;
        } else return false;
    }

    public ShopWorker getManager() {
        return manager;
    }

    private void setManager(ShopWorker manager) throws InvalidInputException {
        if (manager == null) {
            throw new InvalidInputException("invalid manager");
        }
        if (manager.getRole() != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        this.manager = manager;
    }

    protected void createManagerRelation(ShopWorker manager) throws InvalidInputException {
        if (manager == null) {
            throw new InvalidInputException("invalid manager");
        }
        if (manager.getRole() != Role.MANAGER) {
            throw new InvalidInputException("invalid role");
        }
        setManager(manager);
        manager.addOrder(this);
    }

    public ShopWorker getTechnician() {
        return technician;
    }

    private void setTechnician(ShopWorker technician) throws InvalidInputException {
        if (technician != null) {
            if (technician.getRole() != Role.TECHNICIAN) {
                throw new InvalidInputException("invalid role");
            }
        }
        this.technician = technician;
    }

    protected void createTechnicianRelation(ShopWorker technician) throws InvalidInputException {
        if (technician == null) {
            setTechnician(null);
        } else {
            setTechnician(technician);
            technician.addOrder(this);
        }
    }

    public List<OrderItem> getOrderItems() {
        if (orderItems == null) {
            return null;
        }
        return Collections.unmodifiableList(orderItems);
    }

    private void removeOrderItem(OrderItem orderItem) {
        if (orderItem != null) {
            orderItems.remove(orderItem);
        }
    }

    protected void addOrderItem(OrderItem orderItem) throws InvalidInputException {
        if (checkIfEditable()) {
            if (orderItems == null) {
                orderItems = new ArrayList<>();
            }
            if (orderItem != null) {
                orderItems.add(orderItem);
            } else throw new InvalidInputException("invalid orderItem");
        } else throw new InvalidInputException("cant edit the order");
    }

    protected void removeOrderItemFromThis(Item item) throws InvalidInputException {
        if (orderItems != null) {
            if (checkIfEditable()) {
                if (item != null) {
                    for (OrderItem orderItem : orderItems) {
                        if (orderItem.getItem() == item) {
                            removeOrderItem(orderItem);
                            item.removeItemOrder(orderItem);
                            orderItem.removeOrderItemFromExtent();
                            break;
                        }
                    }
                } else throw new InvalidInputException("invalid item");
            } else throw new InvalidInputException("cant edit the order");
        } else throw new InvalidInputException("no items");
    }

    public void setItemRelation(Item item, int quantity) throws InvalidInputException {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        boolean flag = false;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getItem() == item) {
                flag = true;
                break;
            }
        }
        if (item != null) {
            if (quantity > 0 && !flag) {
                try {
                    OrderItem orderItem = new OrderItem(this, item, quantity);
                    this.addOrderItem(orderItem);
                    item.addOrderItem(orderItem);
                } catch (InvalidInputException e) {
                    throw e;
                }
            } else throw new InvalidInputException("could not create relation order-item");
        } else throw new InvalidInputException("invalid item");
        if (flag) {
            throw new InvalidInputException("there exists association with this item");
        }
    }

    public void updateItemRelation(Item item, int newQuantity) throws InvalidInputException {
        if (orderItems != null) {
            boolean flag = false;
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getItem() == item) {
                    flag = true;
                    break;
                }
            }
            if (item != null) {
                if (newQuantity > 0 && flag) {
                    try {
                        this.removeOrderItemFromThis(item);
                        item.removeItemOrderFromThis(this);
                        OrderItem orderItem = new OrderItem(this, item, newQuantity);
                        this.addOrderItem(orderItem);
                        item.addOrderItem(orderItem);
                    } catch (InvalidInputException e) {
                        throw e;
                    }
                } else throw new InvalidInputException("could not find relation order-item");
            } else throw new InvalidInputException("invalid item or quantity");
            if (!flag) {
                throw new InvalidInputException("there does not exists association with this item");
            }
        } else throw new InvalidInputException("no items");
    }

    public void removeItemRelation(Item item) throws InvalidInputException {
        if (orderItems != null) {
            boolean flag = false;
            if (item != null) {
                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getItem() == item) {
                        flag = true;
                        this.removeOrderItemFromThis(item);
                        item.removeItemOrderFromThis(this);
                        orderItem.removeThisFromClassExtent();
                        break;
                    }
                }
            } else throw new InvalidInputException("invalid item");
            if (!flag) {
                throw new InvalidInputException("could not find relation order-item");
            }
        } else throw new InvalidInputException("no items");
    }

    protected void removeOrder() throws InvalidInputException {
        for (OrderItem o : orderItems) {
            removeItemRelation(o.getItem());
        }
        removeThisFromClassExtent();
    }
}
