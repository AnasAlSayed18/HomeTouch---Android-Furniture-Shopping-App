package com.example.hometouch2.Registration;

public class UserAccount {

    private String username;
    private String password;
    private String email;

    private double balance;

    private  String address;

    private String photoRes;

    public UserAccount(String username, String password, String email, String address, String photoRes, double balance) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.photoRes = photoRes;
        this.balance = balance;
    }

    public String getPhotoRes() {
        return photoRes;
    }
    public void setPhotoRes(String photoRes) {
        this.photoRes = photoRes;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
