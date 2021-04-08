package com.maihealthtracker.Models;

public class Users {

    public Users() {
    }

    String name, email_id, b_time, l_time, s_time, d_time, user_id, device_token;

    boolean b_alarm, l_alarm, s_alarm, d_alarm;


    public Users(String name, String email_id, String b_time, String l_time, String s_time, String d_time, String user_id, String device_token, boolean b_alarm, boolean l_alarm, boolean s_alarm, boolean d_alarm) {
        this.name = name;
        this.email_id = email_id;
        this.b_time = b_time;
        this.l_time = l_time;
        this.s_time = s_time;
        this.d_time = d_time;
        this.user_id = user_id;
        this.device_token = device_token;
        this.b_alarm = b_alarm;
        this.l_alarm = l_alarm;
        this.s_alarm = s_alarm;
        this.d_alarm = d_alarm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getB_time() {
        return b_time;
    }

    public void setB_time(String b_time) {
        this.b_time = b_time;
    }

    public String getL_time() {
        return l_time;
    }

    public void setL_time(String l_time) {
        this.l_time = l_time;
    }

    public String getS_time() {
        return s_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public String getD_time() {
        return d_time;
    }

    public void setD_time(String d_time) {
        this.d_time = d_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public boolean isB_alarm() {
        return b_alarm;
    }

    public void setB_alarm(boolean b_alarm) {
        this.b_alarm = b_alarm;
    }

    public boolean isL_alarm() {
        return l_alarm;
    }

    public void setL_alarm(boolean l_alarm) {
        this.l_alarm = l_alarm;
    }

    public boolean isS_alarm() {
        return s_alarm;
    }

    public void setS_alarm(boolean s_alarm) {
        this.s_alarm = s_alarm;
    }

    public boolean isD_alarm() {
        return d_alarm;
    }

    public void setD_alarm(boolean d_alarm) {
        this.d_alarm = d_alarm;
    }
}
