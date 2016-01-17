package com.example.dima.criminalintent.entity;

import java.util.UUID;

/**
 * Created by Dima on 17.01.2016.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    public Crime() {
        // Генерирование уникального идентификатора
        mId = UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
