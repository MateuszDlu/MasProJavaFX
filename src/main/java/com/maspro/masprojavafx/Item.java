package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Item extends ObjectPlus implements Serializable {
    private String name; // name of item
    private int stock; // amount of item available in warehouse
    private List<OrderItem> itemOrders; // connection to orders

    public Item(String name, int stock) throws InvalidInputException {
        super();
        try {
            setName(name);
            setStock(stock);
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) throws InvalidInputException {
        if (name != null && name.length() < 100) {
            this.name = name;
        } else throw new InvalidInputException("invalid name");
    }

    public int getStock() {
        return stock;
    }

    protected void setStock(int stock) throws InvalidInputException {
        if (stock >= 0) {
            this.stock = stock;
        } else throw new InvalidInputException("invalid stock");
    }

    public List<OrderItem> getOrderItems() {
        if (itemOrders == null){
            return null;
        }
        return Collections.unmodifiableList(itemOrders);
    }

    protected void removeItemOrder(OrderItem orderItem) {
        if (orderItem != null) {
            itemOrders.remove(orderItem);
        }
    }

    protected void addOrderItem(OrderItem orderItem) throws InvalidInputException {
        if (itemOrders == null) {
            itemOrders = new ArrayList<>();
        }
        if (orderItem != null) {
            itemOrders.add(orderItem);
        } else throw new InvalidInputException("invalid orderItem");
    }

    protected void removeItemOrderFromThis(Order order) throws InvalidInputException {
        if (order != null) {
            order.removeOrderItemFromThis(this);
        } else throw new InvalidInputException("invalid order");
    }

    public void setOrderRelation(Order order, int quantity) throws InvalidInputException {
        if (itemOrders == null) {
            itemOrders = new ArrayList<>();
        }
        if (order != null && quantity > 0) {
            order.setItemRelation(this, quantity);
        } else throw new InvalidInputException("invalid order or quantity");
    }

    public void updateOrderRelation(Order order, int newQuantity) throws InvalidInputException {
        if (itemOrders != null) {
            if (order != null && newQuantity > 0) {
                order.updateItemRelation(this, newQuantity);
            } else throw new InvalidInputException("invalid order or quantity");
        } else throw new InvalidInputException("no orders");
    }

    public void removeOrderRelation(Order order) throws InvalidInputException {
        if (itemOrders != null) {
            if (order != null) {
                order.removeItemRelation(this);
            } else throw new InvalidInputException("invalid order");
        } else throw new InvalidInputException("no orders");
    }

    public void deleteItem() throws InvalidInputException {
        for (OrderItem orderItem : itemOrders) {
            orderItem.getOrder().removeItemRelation(this);
        }
        removeThisFromClassExtent();
    }
}
