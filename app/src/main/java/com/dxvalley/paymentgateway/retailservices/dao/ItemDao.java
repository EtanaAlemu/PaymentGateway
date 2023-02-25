package com.dxvalley.paymentgateway.retailservices.dao;


import androidx.room.*;

import com.dxvalley.paymentgateway.retailservices.models.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item_db")
    List<Item> getItemList();

    @Query("SELECT * FROM item_db WHERE id in (:id)")
    List<Item> getItem(int[] id);
    @Insert
    void insertItem(Item item);
    @Update
    void updateItem(Item item);
    @Delete
    void deleteItem(Item item);
}
