package com.example.escapetour;

public class DescriptionModel {
    private String name;
    private String address;
    private String description;
    private String hours;
    private String facilities;
    private String website;
    private String[] imgUrls;
    private String phone;
    private Double latitude;
    private Double longitude;
    private int images;

    public DescriptionModel() {
    }


    public DescriptionModel(String name, String address, String description, String hours, String facilities, int images,
                            String website, String[] imgUrls, String phone, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.hours = hours;
        this.facilities = facilities;
        this.website = website;
        this.imgUrls = imgUrls;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
    }

    // getters and setters
    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String[] getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String[] imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

