package com.google.android.gms.samples.vision.ocrreader.utils;

public class ElementModel {
    public static int TYPE_KNOWMORE_VIDEO=0;
    public static int TYPE_KNOWMORE_URL=1;
    public static int TYPE_KNOWMORE_MAP=3;
    public static int TYPE_KNOWMORE_DOCUMENT=2;
    private int id;
    private String uniquename;
    private String title;
    private String description;
    private String knowmore;
    private int typeknowmore;
    private String titleknowmore;

    private int photo;
    private String type;
    private String colour;
    private Type typeElement;

    public int getTypeknowmore() {
        return typeknowmore;
    }

    public void setTypeknowmore(int typeknowmore) {
        this.typeknowmore = typeknowmore;
    }

    public String getTitleknowmore() {
        return titleknowmore;
    }

    public void setTitleknowmore(String titleknowmore) {
        this.titleknowmore = titleknowmore;
    }

    public Type getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(Type typeElement) {
        this.typeElement = typeElement;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public ElementModel() {
    }

    public ElementModel(int id, String uniquename, String title, String description, String knowmore, int photo) {
        this.id = id;
        this.uniquename = uniquename;
        this.title = title;
        this.description = description;
        this.knowmore = knowmore;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniquename() {
        return uniquename;
    }

    public void setUniquename(String uniquename) {
        this.uniquename = uniquename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKnowmore() {
        return knowmore;
    }

    public void setKnowmore(String knowmore) {
        this.knowmore = knowmore;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
