package com.ujwal.see;

import java.io.Serializable;

public class InterestedModel implements Serializable {
    String user_name, email, mobile, address, user_image, interested_date, event_name, event_image;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getInterested_date() {
        return interested_date;
    }

    public void setInterested_date(String interested_date) {
        this.interested_date = interested_date;
    }
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }


    public InterestedModel(String user_name, String email, String mobile, String address, String user_image, String interested_date, String event_name, String event_image) {
        this.user_name = user_name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.user_image = user_image;
        this.interested_date = interested_date;
        this.event_name = event_name;
        this.event_image = event_image;
    }
}
