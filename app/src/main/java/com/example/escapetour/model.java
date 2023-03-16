package com.example.escapetour;

public class model {
    public String name, img1url, id, city;
    public Double latitude, longitude;


    public model() {
    }

    public model(String name, String img1url, String id, String city, Double latitude, Double longitude) {
        this.name = name;
        this.img1url = img1url;
        this.id = id;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return img1url;
    }

    public String getId() {
        return id;
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
