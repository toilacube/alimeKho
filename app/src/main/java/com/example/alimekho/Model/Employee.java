package com.example.alimekho.Model;

import java.io.Serializable;

public class Employee implements Serializable {
    private String id;
    private String name;
    private String title;
    private String dayOfBirth;
    private String identify;
    private String phoneNumber;

    public Employee() {
    }

    public Employee(String id, String name, String title, String dayOfBirth, String identify, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.dayOfBirth = dayOfBirth;
        this.identify = identify;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", identify='" + identify + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
