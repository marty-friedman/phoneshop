package com.es.phoneshop.core.stock.model;

import com.es.phoneshop.core.phone.model.Phone;

public class Stock {
    private Phone phone;
    private Long stock;
    private Long reserved;

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }
}
