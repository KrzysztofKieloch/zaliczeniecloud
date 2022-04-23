package com.example.zaliczenie;

public class MainModel {
    String marka,model,purl;

    MainModel(){

    }
    public MainModel(String marka, String model, String purl) {
        this.marka = marka;
        this.model = model;
        this.purl = purl;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
