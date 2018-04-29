package com.es.phoneshop.web.controller.form;

import com.es.phoneshop.core.order.model.Order;
import org.hibernate.validator.constraints.NotEmpty;

public class OrderPageForm {
    private Order order;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String deliveryAddress;
    @NotEmpty
    private String contactPhoneNo;
    private String additionalInformation;
    private Object stocksAvailable;

    public void applyDataToAnOrder() {
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setDeliveryAddress(deliveryAddress);
        order.setContactPhoneNo(contactPhoneNo);
        order.setAdditionalInformation(additionalInformation);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Object getStocksAvailable() {
        return stocksAvailable;
    }

    public void setStocksAvailable(Object stocksAvailable) {
        this.stocksAvailable = stocksAvailable;
    }
}
