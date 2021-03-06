package com.es.phoneshop.core.cart.throwable;

import java.util.Set;

public class TooBigQuantityException extends RuntimeException {
    private Set<Long> phoneIds;

    public TooBigQuantityException() {
    }

    public TooBigQuantityException(Set<Long> phoneIds) {
        this.phoneIds = phoneIds;
    }

    public Set<Long> getPhoneIds() {
        return phoneIds;
    }
}
