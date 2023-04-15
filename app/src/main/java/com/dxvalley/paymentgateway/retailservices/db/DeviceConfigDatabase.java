package com.dxvalley.paymentgateway.retailservices.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dxvalley.paymentgateway.retailservices.dao.DeviceConfigDao;
import com.dxvalley.paymentgateway.retailservices.models.DeviceConfig;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;


@Database(entities = DeviceConfig.class, exportSchema = false, version = 1)
public abstract class DeviceConfigDatabase extends RoomDatabase {
    private static final String DB_NAME = "device_config";
    private static DeviceConfigDatabase instance;

    public static synchronized DeviceConfigDatabase getInstance(Context context){
        if(instance == null){

            final byte[] passphrase = SQLiteDatabase.getBytes("5bcfe932d620d88f6c382880e1b8826f".toCharArray());
            final SupportFactory factory = new SupportFactory(passphrase);
            instance = Room.databaseBuilder(context.getApplicationContext(), DeviceConfigDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().openHelperFactory(factory).build();
        }
        return instance;
    }

    public abstract DeviceConfigDao configDao();
}
