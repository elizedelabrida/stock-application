package com.elize.stock.retrofit.service;

import com.elize.stock.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductService {
    @GET("produto")
    Call<List<Product>> getAll();

    @POST("produto")
    Call<Product> save(@Body Product product);

    @PUT("produto/{id}")
    Call<Product> update(@Path("id") long id, @Body Product product);

    @DELETE("produto/{id}")
    Call<Void> remove(@Path("id") long id);
}
