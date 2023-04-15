package com.dxvalley.paymentgateway.retailservices.db;

import android.content.Context;

import androidx.room.*;

import com.dxvalley.paymentgateway.retailservices.dao.ItemDao;
import com.dxvalley.paymentgateway.retailservices.models.Item;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = Item.class, exportSchema = false, version = 1)
public abstract class ItemDatabase extends RoomDatabase {
    private static final String DB_NAME = "item_db";
    private static ItemDatabase instance;

    public static synchronized ItemDatabase getInstance(Context context){
        if(instance == null){
//            instance = Room.databaseBuilder(context.getApplicationContext(), ItemDatabase.class, DB_NAME)
//                    .fallbackToDestructiveMigration().build();

            final byte[] passphrase = SQLiteDatabase.getBytes("5bcfe932d620d88f6c382880e1b8826f".toCharArray());
            final SupportFactory factory = new SupportFactory(passphrase);
            instance = Room.databaseBuilder(context.getApplicationContext(), ItemDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().openHelperFactory(factory).build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();
}
