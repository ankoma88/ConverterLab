package com.ankoma88.converterlab.rest;


import com.ankoma88.converterlab.rest.models.BaseModel;

import retrofit.Callback;
import retrofit.http.GET;

public interface ApiService {

    @GET("/ru/public/currency-cash.json")
    void getBaseModel(Callback<BaseModel> callback);

}