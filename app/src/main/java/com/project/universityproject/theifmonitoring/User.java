package com.project.universityproject.theifmonitoring;

import java.io.Serializable;

/**
 * Created by DibaAndSushoma on 10/15/2017.
 */

public class User implements Serializable{

    private int userId;

    private String userName;
    private String password;
    private String confirmPassword;


    private String userPhone;
    private String verifyingCode;
    private String confirmVerifyingCode;
    private String emailId;

    private String userDeviceId;

    private String userSIMID;

    private double userLat;

    private double userLang;


    private String deviceCurrentSIMID;


    public User(){

    }


    public User(String userEmail, String password, String confirmPassword) {
        this.userName = userEmail;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }


    public User(String userEmail, String password, String confirmPassword, String userPhone, String verifyingCode, String confirmVerifyingCode, String emailId) {
        this.userName = userEmail;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.userPhone = userPhone;
        this.verifyingCode = verifyingCode;
        this.confirmVerifyingCode = confirmVerifyingCode;
        this.emailId = emailId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserDeviceId() {
        return userDeviceId;
    }

    public void setUserDeviceId(String userDeviceId) {
        this.userDeviceId = userDeviceId;
    }


    public String getUserSIMID() {
        return userSIMID;
    }

    public void setUserSIMID(String userSIMID) {
        this.userSIMID = userSIMID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getVerifyingCode() {
        return verifyingCode;
    }

    public void setVerifyingCode(String verifyingCode) {
        this.verifyingCode = verifyingCode;
    }

    public String getConfirmVerifyingCode() {
        return confirmVerifyingCode;
    }

    public void setConfirmVerifyingCode(String confirmVerifyingCode) {
        this.confirmVerifyingCode = confirmVerifyingCode;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLang() {
        return userLang;
    }

    public void setUserLang(double userLang) {
        this.userLang = userLang;
    }

    public String getDeviceCurrentSIMID() {
        return deviceCurrentSIMID;
    }

    public void setDeviceCurrentSIMID(String deviceCurrentSIMID) {
        this.deviceCurrentSIMID = deviceCurrentSIMID;
    }
}
