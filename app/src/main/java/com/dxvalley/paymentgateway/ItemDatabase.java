package com.dxvalley.paymentgateway;

import android.content.Context;

import androidx.room.*;

@Database(entities = Item.class, exportSchema = false, version = 1)
public abstract class ItemDatabase extends RoomDatabase {
    private static final String DB_NAME = "item_db";
    private static ItemDatabase instance;

    public static synchronized ItemDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ItemDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();
}
