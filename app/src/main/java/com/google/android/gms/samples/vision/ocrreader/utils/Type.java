package com.google.android.gms.samples.vision.ocrreader.utils;

public class Type {
    public static int TYPE_INSTRUMENT =0;
    public static int TYPE_MEDIA =1;
    public static int TYPE_CIRCUIT=2;
    public static int TYPE_ASSEMBLING=3;
    //public static int TYPE_GEOGRAPHY=4;

    public static String COLOR_INSTRUMENT ="#00e68a";
    public static String COLOR_MEDIA ="#ff4d4d";
    public static String COLOR_CIRCUIT ="#66a3ff";
    public static String COLOR_ASSEMBLING="#e6b3cc";
    //public static String COLOR_GEO="#000000";
    private int type;
    private String name;
    private String color;

    public Type(int type, String name, String color) {
        this.type = type;
        this.name = name;
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
