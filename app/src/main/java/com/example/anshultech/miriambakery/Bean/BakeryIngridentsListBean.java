package com.example.anshultech.miriambakery.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BakeryIngridentsListBean implements Parcelable {

    @SerializedName("quantity")
    private double quantity;
    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredient;

    public BakeryIngridentsListBean(){}

    protected BakeryIngridentsListBean(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<BakeryIngridentsListBean> CREATOR = new Creator<BakeryIngridentsListBean>() {
        @Override
        public BakeryIngridentsListBean createFromParcel(Parcel in) {
            return new BakeryIngridentsListBean(in);
        }

        @Override
        public BakeryIngridentsListBean[] newArray(int size) {
            return new BakeryIngridentsListBean[size];
        }
    };

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}