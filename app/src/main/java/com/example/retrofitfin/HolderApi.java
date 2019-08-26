package com.example.retrofitfin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HolderApi {

    @GET("list.json")
    Call<ResponseGen<List<Post>>> getPosts();

    @GET("{id}.json")
    Call<ResponseGen<DataItem>> getData(@Path("id") int posId);
    
}
