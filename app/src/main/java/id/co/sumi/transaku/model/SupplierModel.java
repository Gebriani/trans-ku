package id.co.sumi.transaku.model;

import java.io.Serializable;

/**
 * Transaku
 * Author Gebriani
 * on 15, March 2017.
 */

public class SupplierModel implements Serializable {
    private long id;
    private String name;
    private String email;
    private String description;
    private String address;
    private CityModel city;
    private int postalCode;
    private String phone;
    private String latitude;
    private String longitude;
    private String createdDate;
    private String picturePath;
    private String pictureBase64;
    private int status;
    private double distance;

    public SupplierModel(long id, String name, String email, String description, String address, CityModel city, int postalCode, String phone, String latitude, String longitude, String createdDate, String picturePath, String pictureBase64, int status, double distance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.description = description;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDate = createdDate;
        this.picturePath = picturePath;
        this.pictureBase64 = pictureBase64;
        this.status = status;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureBase64() {
        return pictureBase64;
    }

    public void setPictureBase64(String pictureBase64) {
        this.pictureBase64 = pictureBase64;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}