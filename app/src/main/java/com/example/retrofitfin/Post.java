package com.example.retrofitfin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("actual_time")
    @Expose
    private long actualTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("location")
    @Expose
    private String location;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getActualTime() {
        return actualTime;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

}
