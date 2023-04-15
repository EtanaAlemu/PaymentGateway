package com.dxvalley.paymentgateway.retailservices.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dxvalley.paymentgateway.retailservices.dao.TipDao;
import com.dxvalley.paymentgateway.retailservices.models.Tip;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = Tip.class, exportSchema = false, version = 1)
public abstract class TipDatabase extends RoomDatabase {
    private static final String DB_NAME = "tip_db";
    private static TipDatabase instance;

    public static synchronized TipDatabase getInstance(Context context){
        if(instance == null){

            final byte[] passphrase = SQLiteDatabase.getBytes("5bcfe932d620d88f6c382880e1b8826f".toCharArray());
            final SupportFactory factory = new SupportFactory(passphrase);
            instance = Room.databaseBuilder(context.getApplicationContext(), TipDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().openHelperFactory(factory).build();
        }
        return instance;
    }

    public abstract TipDao tipDao();
}
