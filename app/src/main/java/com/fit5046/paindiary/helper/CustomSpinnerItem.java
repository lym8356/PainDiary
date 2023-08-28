package com.fit5046.paindiary.helper;

public class CustomSpinnerItem {

    //custom spinner item with name and image
    private String spinnerItemName;
    private int spinnerItemImage;

    public CustomSpinnerItem(String spinnerItemName, int spinnerItemImage) {
        this.spinnerItemName = spinnerItemName;
        this.spinnerItemImage = spinnerItemImage;
    }

    public String getSpinnerItemName() {
        return spinnerItemName;
    }

    public int getSpinnerItemImage() {
        return spinnerItemImage;
    }
}