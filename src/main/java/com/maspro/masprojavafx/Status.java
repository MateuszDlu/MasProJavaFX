package com.maspro.masprojavafx;

public enum Status { // status of an order. order can be modified only in AWAITING_APPROVAL state
    AWAITING_APPROVAL,
    APPROVED,
    AWAITING_DELIVERY,
    DELIVERED,
    CANCELLED
}
