package com.gisfy.inclenJson.PayLoads.Questions;

import com.google.gson.annotations.SerializedName;

public class Properties{
    @SerializedName("Size")
    private String size;
    @SerializedName("Mandatory")
    private boolean mandatory;

    public Properties() {
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }


}