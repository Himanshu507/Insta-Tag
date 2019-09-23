package com.tagfeature;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tagfeature.models.TaggedPhoto;

import java.util.ArrayList;


public class InstaTagApplication extends Application {
    private static InstaTagApplication sInstaTagSampleApplication;
    private static final String SHARED_PREF_NAME = "insta_tag_preferences";
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public InstaTagApplication() {
        sInstaTagSampleApplication = this;
    }

    public static InstaTagApplication getInstance() {
        if (sInstaTagSampleApplication == null) {
            sInstaTagSampleApplication = new InstaTagApplication();
        }
        if (sInstaTagSampleApplication.mSharedPreferences == null) {
            sInstaTagSampleApplication.mSharedPreferences =
                    sInstaTagSampleApplication.getSharedPreferences(SHARED_PREF_NAME,
                            Context.MODE_PRIVATE);
        }

        return sInstaTagSampleApplication;
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private String getString(String key) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getString(key, "");
        }

        return "";
    }

    private void putString(String key, String value) {
        try {
            if (mSharedPreferences != null) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(SHARED_PREF_NAME, "Unable Put String in Shared preference", e);
        }
    }


    public ArrayList<TaggedPhoto> getTaggedPhotos() {
        String json = getString(Keys.TAGGED_PHOTOS.getKeyName());
        ArrayList<TaggedPhoto> taggedPhotoArrayList;
        if (!json.equals("")) {
            taggedPhotoArrayList =
                    new Gson().fromJson(json, new TypeToken<ArrayList<TaggedPhoto>>() {
                    }.getType());
        } else {
            taggedPhotoArrayList = new ArrayList<>();
        }
        return taggedPhotoArrayList;
    }

    public void setTaggedPhotos(ArrayList<TaggedPhoto> taggedPhotoArrayList) {
        putString(Keys.TAGGED_PHOTOS.getKeyName(), toJson(taggedPhotoArrayList));
    }

    private enum Keys {
        TAGGED_PHOTOS("TAGGED_PHOTOS");
        private final String keyName;

        Keys(String label) {
            this.keyName = label;
        }

        public String getKeyName() {
            return keyName;
        }
    }


    public static String toJson(Object object) {
        try {
            Gson gson = new Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(SHARED_PREF_NAME, "Error In Converting ModelToJson", e);
        }
        return "";
    }

}
