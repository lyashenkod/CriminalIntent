package com.example.dima.criminalintent.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dima on 04.02.2016.
 */
public class Photo {

    private static final String JSON_FILENAME = "filename";
    private String mFilename;

    /**
     * Создание объекта Photo, представляющего файл на диске
     */
    public Photo(String filename) {
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }


}
