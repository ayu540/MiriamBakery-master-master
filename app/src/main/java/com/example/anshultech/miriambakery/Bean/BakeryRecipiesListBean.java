package com.example.anshultech.miriambakery.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BakeryRecipiesListBean implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<BakeryIngridentsListBean>  bakeryIngridentsListBeans;
    @SerializedName("steps")
    private ArrayList<BakeryStepsListBean> bakeryStepsListBeans;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;
    private String loggedUserName;

    public BakeryRecipiesListBean(){}

    protected BakeryRecipiesListBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        bakeryIngridentsListBeans = in.createTypedArrayList(BakeryIngridentsListBean.CREATOR);
        bakeryStepsListBeans = in.createTypedArrayList(BakeryStepsListBean.CREATOR);
        servings = in.readInt();
        image = in.readString();
        loggedUserName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(bakeryIngridentsListBeans);
        dest.writeTypedList(bakeryStepsListBeans);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeString(loggedUserName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BakeryRecipiesListBean> CREATOR = new Creator<BakeryRecipiesListBean>() {
        @Override
        public BakeryRecipiesListBean createFromParcel(Parcel in) {
            return new BakeryRecipiesListBean(in);
        }

        @Override
        public BakeryRecipiesListBean[] newArray(int size) {
            return new BakeryRecipiesListBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<BakeryIngridentsListBean> getBakeryIngridentsListBeans() {
        return bakeryIngridentsListBeans;
    }

    public void setBakeryIngridentsListBeans(ArrayList<BakeryIngridentsListBean> bakeryIngridentsListBeans) {
        this.bakeryIngridentsListBeans = bakeryIngridentsListBeans;
    }

    public ArrayList<BakeryStepsListBean> getBakeryStepsListBeans() {
        return bakeryStepsListBeans;
    }

    public void setBakeryStepsListBeans(ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {
        this.bakeryStepsListBeans = bakeryStepsListBeans;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLoggedUserName() {
        return loggedUserName;
    }

    public void setLoggedUserName(String loggedUserName) {
        this.loggedUserName = loggedUserName;
    }
}