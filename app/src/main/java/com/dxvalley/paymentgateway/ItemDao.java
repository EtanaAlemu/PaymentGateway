package com.dxvalley.paymentgateway;


import androidx.room.*;

import java.util.ArrayList;
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
