package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;

public class OrderItem extends ObjectPlus implements Serializable {
    private Order order; // assigned to order
    private Item item; // ordered item
    private int quantity; // quantity of the item

    protected OrderItem(Order order, Item item, int quantity) throws InvalidInputException {
        super();
        try {
            if (item.getStock() < quantity) {
                throw new InvalidInputException("not enough items in stock");
            } else if (order != null) {
                if (order.getStatus() == Status.APPROVED || order.getStatus() == Status.AWAITING_DELIVERY || order.getStatus() == Status.DELIVERED || order.getStatus() == Status.CANCELLED) {
                    throw new InvalidInputException("status of order invalid");
                } else {
                    setOrder(order);
                    setItem(item);
                    setQuantity(quantity);
                }
            } else {
                throw new InvalidInputException("invalid order");
            }
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }
    }

    public Order getOrder() {
        return order;
    }

    private void setOrder(Order order) throws InvalidInputException {
        if (order != null) {
            this.order = order;
        } else throw new InvalidInputException("invalid order");
    }

    public Item getItem() {
        return item;
    }

    private void setItem(Item item) throws InvalidInputException {
        if (item != null) {
            this.item = item;
        } else throw new InvalidInputException("invalid item");
    }

    public int getQuantity() {
        return quantity;
    }

    private void setQuantity(int quantity) throws InvalidInputException {
        if (quantity <= 0) {
            throw new InvalidInputException("invalid quantity");
        } else {
            this.quantity = quantity;
        }
    }

    public void checkOrderStatus() throws InvalidInputException {
        if (order.getStatus() == Status.AWAITING_DELIVERY || order.getStatus() == Status.DELIVERED) {
            item.setStock(item.getStock() - quantity);
        }
    }

    protected void removeOrderItemFromExtent() {
        this.item = null;
        this.order = null;
        removeThisFromClassExtent();
    }
}
