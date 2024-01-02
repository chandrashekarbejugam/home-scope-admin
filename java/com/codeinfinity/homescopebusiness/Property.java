package com.codeinfinity.homescopebusiness;

public class Property {
    private String name;
    private String price;
    private String description;
    private String address;

    private String uid;

    private String date;
    private String facilities;
    private String areaSqft;
    private String bedrooms;
    private String livingRooms;
    private String bathrooms;
    private String image, propertyType;

    public Property() {
        // Required empty constructor for Firebase
    }

    // Getter and setter methods for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaSqft() {
        return areaSqft;
    }

    public void setAreaSqft(String areaSqft) {
        this.areaSqft = areaSqft;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getLivingRooms() {
        return livingRooms;
    }

    public void setLivingRooms(String livingRooms) {
        this.livingRooms = livingRooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getImage() {
        return image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

