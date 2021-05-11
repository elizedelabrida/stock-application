package com.elize.stock.retrofit;

import com.elize.stock.retrofit.service.ProductService;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockRetrofit {

    public static final String BASE_URL = "http://10.22.226.15:8080/";

    public ProductService getProductService() {
        return productService;
    }

    final ProductService productService;

    public StockRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient client = configureClient(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        productService = retrofit.create(ProductService.class);
    }

    @NotNull
    private OkHttpClient configureClient(HttpLoggingInterceptor logging) {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
