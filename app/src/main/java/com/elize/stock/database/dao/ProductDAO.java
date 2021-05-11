package com.elize.stock.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.elize.stock.model.Product;

import java.util.List;

@Dao
public interface ProductDAO {

    @Insert
    long save(Product product);

    @Update
    void update(Product product);

    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM Product WHERE id = :id")
    Product getProduct(long id);

    @Delete
    void remove(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<Product> newProducts);
}
