package com.example.retrofitfin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseGen<T> extends BaseResponse {

    @SerializedName("data")
    @Expose
    private T data;

    public T getData() {
        return data;
    }
}
