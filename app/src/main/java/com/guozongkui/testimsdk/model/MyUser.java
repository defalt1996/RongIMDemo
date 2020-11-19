package com.guozongkui.testimsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyUser implements Parcelable {
    private String userId;
    private String portraitUri;
    private String sex;
    private String name;
    private String token;
    private int type;

    protected MyUser(Parcel in) {
        userId = in.readString();
        portraitUri = in.readString();
        sex = in.readString();
        name = in.readString();
        token = in.readString();
        type = in.readInt();
    }

    public static final Creator<MyUser> CREATOR = new Creator<MyUser>() {
        @Override
        public MyUser createFromParcel(Parcel in) {
            return new MyUser(in);
        }

        @Override
        public MyUser[] newArray(int size) {
            return new MyUser[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(portraitUri);
        parcel.writeString(sex);
        parcel.writeString(name);
        parcel.writeString(token);
        parcel.writeInt(type);
    }
}
