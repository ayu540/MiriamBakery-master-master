package com.example.anshultech.miriambakery.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BakeryStepsListBean implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("shortDescription")
    private String shortDescription;
    @SerializedName("description")
    private String description;
    @SerializedName("videoURL")
    private String videoURL;
    @SerializedName("thumbnailURL")
    private String thumbnailURL;
    @SerializedName("userName")
    private String userName;

//    private boolean isChecked;

    public BakeryStepsListBean() {
    }
    public BakeryStepsListBean(int id, String shortDescription,
                               String description, String videoURL,
                               String thumbnailURL, String userName) {
        this.id=id;
        this.shortDescription=shortDescription;
        this.description=description;
        this.videoURL=videoURL;
        this.thumbnailURL=thumbnailURL;
        this.userName=userName;
    }

    protected BakeryStepsListBean(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
        userName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
        dest.writeString(userName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BakeryStepsListBean> CREATOR = new Creator<BakeryStepsListBean>() {
        @Override
        public BakeryStepsListBean createFromParcel(Parcel in) {
            return new BakeryStepsListBean(in);
        }

        @Override
        public BakeryStepsListBean[] newArray(int size) {
            return new BakeryStepsListBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}