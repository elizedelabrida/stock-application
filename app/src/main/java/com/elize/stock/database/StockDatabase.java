package com.elize.stock.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.elize.stock.database.converter.BigDecimalConverter;
import com.elize.stock.database.dao.ProductDAO;
import com.elize.stock.model.Product;

@Database(entities = {Product.class}, version = 2, exportSchema = false)
@TypeConverters(value = {BigDecimalConverter.class})
public abstract class StockDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "stock.db";

    public abstract ProductDAO getProductDAO();

    public static StockDatabase getInstance(Context context) {
        return Room.databaseBuilder(
                context,
                StockDatabase.class,
                DATABASE_NAME)
                .build();
    }
}
