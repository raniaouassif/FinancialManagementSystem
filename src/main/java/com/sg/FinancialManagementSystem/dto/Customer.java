package com.sg.FinancialManagementSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Customer {
    private  int customerID;
    @NotBlank(message = "First name must not be blank")
    @Size(max = 50, message = "First name must be fewer than 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters.")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 50, message = "Last name must be fewer than 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters.")
    private String lastName;

    @NotBlank(message = "Phone number must not be blank")
    @Size(min=17, max = 17, message = "Phone number must be 10 digits.")
    @Pattern(regexp = "^\\+1 \\([0-9]{3}\\) [0-9]{3}-[0-9]{4}$", message = "Invalid phone format")
    private String phoneNumber;
    private List<Account> accounts;
    private Portfolio portfolio;

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getCustomerID() == customer.getCustomerID() && Objects.equals(getFirstName(), customer.getFirstName()) && Objects.equals(getLastName(), customer.getLastName()) && Objects.equals(getPhoneNumber(), customer.getPhoneNumber()) && Objects.equals(getAccounts(), customer.getAccounts()) && Objects.equals(getPortfolio(), customer.getPortfolio());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerID(), getFirstName(), getLastName(), getPhoneNumber(), getAccounts(), getPortfolio());
    }
}
