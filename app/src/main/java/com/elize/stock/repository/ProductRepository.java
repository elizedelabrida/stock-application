package com.elize.stock.repository;

import android.content.Context;

import com.elize.stock.asynctask.BaseAsyncTask;
import com.elize.stock.database.StockDatabase;
import com.elize.stock.database.dao.ProductDAO;
import com.elize.stock.model.Product;
import com.elize.stock.retrofit.StockRetrofit;
import com.elize.stock.retrofit.callback.BaseCallback;
import com.elize.stock.retrofit.callback.VoidCallBack;
import com.elize.stock.retrofit.service.ProductService;

import java.util.List;

import retrofit2.Call;

public class ProductRepository {

    private final ProductDAO dao;
    final ProductService productService;

    public ProductRepository(Context context) {
        dao = getProductDao(context);
        this.productService = new StockRetrofit().getProductService();
    }

    private ProductDAO getProductDao(Context context) {
        StockDatabase db = StockDatabase.getInstance(context);
        return db.getProductDAO();
    }

    public void getProducts(DataDoneCallback<List<Product>> callback) {
        getInternalDatabaseProducts(callback);
    }

    private void getInternalDatabaseProducts(DataDoneCallback<List<Product>> callback) {
        new BaseAsyncTask<>(dao::getAll,
                result -> {
                    callback.whenSuccessful(result);
                    getProductsFromApi(callback);
                })
                .execute();
    }

    private void getProductsFromApi(DataDoneCallback<List<Product>> callback) {

        Call<List<Product>> call = productService.getAll();
        call.enqueue(new BaseCallback<>(new BaseCallback.ResponseCallback<List<Product>>() {
            @Override
            public void whenSuccessful(List<Product> newProducts) {
                addToInternalDatabase(newProducts, callback);
            }

            @Override
            public void whenFail(int error) {
                callback.whenFail(error);
            }
        }));
    }

    private void addToInternalDatabase(List<Product> products, DataDoneCallback<List<Product>> callback) {
        new BaseAsyncTask<>(() -> {
            dao.save(products);
            return dao.getAll();
        }, callback::whenSuccessful)
                .execute();
    }

    public void save(Product product, DataDoneCallback<Product> callback) {
        saveProductApi(product, callback);

    }

    private void saveProductApi(Product product,
                                DataDoneCallback<Product> callback) {
        Call<Product> response = productService.save(product);
        response.enqueue(new BaseCallback<>(new BaseCallback.ResponseCallback<Product>() {
            @Override
            public void whenSuccessful(Product productSaved) {
                saveProductInternalDatabase(productSaved, callback);
            }

            @Override
            public void whenFail(int error) {
                callback.whenFail(error);
            }
        }));
    }

    private void saveProductInternalDatabase(Product product, DataDoneCallback<Product> callback) {
        new BaseAsyncTask<>(() -> {
            long id = dao.save(product);
            return dao.getProduct(id);
        }, callback::whenSuccessful)
                .execute();
    }

    public void update(Product product, DataDoneCallback<Product> callback) {
        updateProductApi(product, callback);

    }

    private void updateProductApi(Product product, DataDoneCallback<Product> callback) {
        Call<Product> call = productService.update(product.getId(), product);
        call.enqueue(new BaseCallback<>(new BaseCallback.ResponseCallback<Product>() {
            @Override
            public void whenSuccessful(Product productUpdated) {
                updateToInternalDatabase(productUpdated, callback);
            }

            @Override
            public void whenFail(int error) {
                callback.whenFail(error);
            }
        }));
    }

    private void updateToInternalDatabase(Product product, DataDoneCallback<Product> callback) {
        new BaseAsyncTask<>(() -> {
            dao.update(product);
            return product;
        }, callback::whenSuccessful)
                .execute();
    }

    public void remove(Product product, DataDoneCallback<Void> callback) {
        removeProductApi(product, callback);
    }

    private void removeProductApi(Product product, DataDoneCallback<Void> callback) {
        Call<Void> call = productService.remove(product.getId());
        call.enqueue(new VoidCallBack(new VoidCallBack.ResponseCallback<Void>() {
            @Override
            public void whenSuccessful() {
                removeProductInternalDatabase(product, callback);
            }

            @Override
            public void whenFail(int error) {
                callback.whenFail(error);
            }
        }));
    }

    private void removeProductInternalDatabase(Product product, DataDoneCallback<Void> callback) {
        new BaseAsyncTask<>(() -> {
            dao.remove(product);
            return null;
        }, callback::whenSuccessful)
                .execute();
    }


    public interface DataDoneCallback<T> {
        void whenSuccessful(T result);

        void whenFail(int error);
    }
}
