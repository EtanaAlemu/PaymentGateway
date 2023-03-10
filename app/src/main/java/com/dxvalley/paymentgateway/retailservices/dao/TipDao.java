package com.dxvalley.paymentgateway.retailservices.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dxvalley.paymentgateway.retailservices.models.Tip;

import java.util.List;

@Dao
public interface TipDao {
    @Query("SELECT * FROM tip_db")
    List<Tip> getTipList();
    @Insert
    void insertTip(Tip tip);
    @Update
    void updateTip(Tip tip);
    @Delete
    void deleteTip(Tip tip);
}
