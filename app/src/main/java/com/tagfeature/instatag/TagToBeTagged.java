package com.tagfeature.instatag;

import android.os.Parcel;
import android.os.Parcelable;

public class TagToBeTagged implements Parcelable {
    private String unique_tag_id;
    private float x_co_ord;
    private float y_co_ord;

    public TagToBeTagged(String unique_tag_id, float x_co_ord, float y_co_ord) {
        this.unique_tag_id = unique_tag_id;
        this.x_co_ord = x_co_ord;
        this.y_co_ord = y_co_ord;

    }

    public String getUnique_tag_id() {
        return unique_tag_id;
    }

    public void setUnique_tag_id(String unique_tag_id) {
        this.unique_tag_id = unique_tag_id;
    }

    private TagToBeTagged(Parcel in) {
        this.unique_tag_id = in.readString();
        this.x_co_ord = (float) in.readValue(float.class.getClassLoader());
        this.y_co_ord = (float) in.readValue(float.class.getClassLoader());
    }

    public float getX_co_ord() {
        return x_co_ord;
    }

    public void setX_co_ord(float x_co_ord) {
        this.x_co_ord = x_co_ord;
    }

    public float getY_co_ord() {
        return y_co_ord;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.unique_tag_id);
        dest.writeValue(this.x_co_ord);
        dest.writeValue(this.y_co_ord);
    }

    public void setY_co_ord(float y_co_ord) {
        this.y_co_ord = y_co_ord;
    }

    public static final Creator<TagToBeTagged> CREATOR =
            new Creator<TagToBeTagged>() {
                @Override
                public TagToBeTagged createFromParcel(Parcel source) {
                    return new TagToBeTagged(source);
                }

                @Override
                public TagToBeTagged[] newArray(int size) {
                    return new TagToBeTagged[size];
                }
            };
}
