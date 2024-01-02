package com.codeinfinity.homescopebusiness;

public class Requests {
    private String name;
    private String price;

    String userName;

    String userNumber;
    String userUid;

    public Requests() {
        // Empty constructor required for Firebase
    }

    public Requests(String name, String price) {
        this.name = name;
        this.price = price;

    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}

