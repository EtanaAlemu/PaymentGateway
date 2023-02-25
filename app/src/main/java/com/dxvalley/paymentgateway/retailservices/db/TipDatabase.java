package com.dxvalley.paymentgateway.retailservices.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dxvalley.paymentgateway.retailservices.dao.TipDao;
import com.dxvalley.paymentgateway.retailservices.models.Tip;

@Database(entities = Tip.class, exportSchema = false, version = 1)
public abstract class TipDatabase extends RoomDatabase {
    private static final String DB_NAME = "tip_db";
    private static TipDatabase instance;

    public static synchronized TipDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), TipDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract TipDao tipDao();
}
