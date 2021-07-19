package net.contal.demo.modal;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class CustomerAccount {
    @Id
    @GeneratedValue
    private long id;
    @OneToMany
    private List<BankTransaction> transactions;

    //TODO implement extra properties and create  setter and getter for each

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private int accountNumber;

    @Column
    private double accountBalance;

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

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    //Set getter and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<BankTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BankTransaction> transactions) {
        this.transactions = transactions;
    }
}
