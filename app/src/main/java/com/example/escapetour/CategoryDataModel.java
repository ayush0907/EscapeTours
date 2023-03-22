package com.example.escapetour;

public class CategoryDataModel {

    private String title, category;
    private int imgid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CategoryDataModel(String title, int imgid, String category) {
        this.title = title;
        this.imgid = imgid;
        this.category = category;
    }
}
