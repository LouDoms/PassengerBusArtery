package com.itproject.piczon.loudoms.passengerbusartery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LouDoms on 11/30/2017.
 */

public class Passenger {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //    @SerializedName("accessToken")
//    @Expose
    private String token;



    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("busNumber")
    @Expose
    private String busNumber;
    @SerializedName("busType")
    @Expose
    private String busType;
    @SerializedName("inTransit")
    @Expose
    private Integer inTransit;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tagId")
    @Expose
    private String tagId;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("numberOfSeats")
    @Expose
    private Integer numberOfSeats;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;


    public Passenger(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType){
        this.busType = busType;
    }

    public Integer getInTransit() {
        return inTransit;
    }

    public void setInTransit(Integer inTransit) {
        this.inTransit = inTransit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

//    @Override
//    public String toString() {
//        return "Passenger{" +
//                "name='" + name + '\'' +
//                ", username='" + username + '\'' +
//                ", password=" + password + '\'' +
//                ", gender=" + gender + '\'' +
//                ", age=" + age +
//                ", balance=" + balance +
//                ", busNumber=" + busNumber + '\'' +
//                ", inTransit=" + inTransit +
//                ", type=" + type + '\'' +
//                ", tagId=" + tagId + '\'' +
//                ", destination=" + destination + '\'' +
//                ", numberOfSeats=" + numberOfSeats +
//                ", longitude=" + longitude +
//                ", latitude=" + latitude +
//                '}';
//    }
}

