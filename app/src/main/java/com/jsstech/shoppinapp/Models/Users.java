package com.jsstech.shoppinapp.Models;

public class Users {
    public String name,phone,password;

    public Users(String name,String phone,String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public Users() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
