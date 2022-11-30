package edu.birzeit.hotelproject.models;

import java.util.Objects;

public class Customer {
    private int customer_id;
    private String customer_name;
    private String customer_username;
    private String customer_password;
    private String customer_visa;
    private String customer_phone;
    private String dateOfBirth;

    public Customer() {
    }

    public Customer(String customer_username, String customer_password) {
        this.customer_username = customer_username;
        this.customer_password = customer_password;
    }

    public Customer(String customer_name, String customer_username, String customer_password, String customer_visa, String customer_phone, String dateOfBirth) {
        this.customer_name = customer_name;
        this.customer_username = customer_username;
        this.customer_password = customer_password;
        this.customer_visa = customer_visa;
        this.customer_phone = customer_phone;
        this.dateOfBirth = dateOfBirth;
    }

    public Customer(int customer_id, String customer_name, String customer_username, String customer_password, String customer_visa, String customer_phone, String dateOfBirth) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_username = customer_username;
        this.customer_password = customer_password;
        this.customer_visa = customer_visa;
        this.customer_phone = customer_phone;
        this.dateOfBirth = dateOfBirth;
    }



    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_username() {
        return customer_username;
    }

    public void setCustomer_username(String customer_username) {
        this.customer_username = customer_username;
    }

    public String getCustomer_password() {
        return customer_password;
    }

    public void setCustomer_password(String customer_password) {
        this.customer_password = customer_password;
    }

    public String getCustomer_visa() {
        return customer_visa;
    }

    public void setCustomer_visa(String customer_visa) {
        this.customer_visa = customer_visa;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customer_username, customer.customer_username) && Objects.equals(customer_password, customer.customer_password);
    }



    @Override
    public String toString() {
        return "Customer{" +
                ", customer_name='" + customer_name + '\'' +
                ", customer_username='" + customer_username + '\'' +
                ", customer_password='" + customer_password + '\'' +
                ", customer_visa='" + customer_visa + '\'' +
                ", customer_phone='" + customer_phone + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
