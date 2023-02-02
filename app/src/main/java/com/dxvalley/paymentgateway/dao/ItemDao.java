package com.dxvalley.paymentgateway.dao;


import androidx.room.*;

import com.dxvalley.paymentgateway.models.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item_db")
    List<Item> getItemList();
    @Insert
    void insertItem(Item item);
    @Update
    void updateItem(Item item);
    @Delete
    void deleteItem(Item item);
}
