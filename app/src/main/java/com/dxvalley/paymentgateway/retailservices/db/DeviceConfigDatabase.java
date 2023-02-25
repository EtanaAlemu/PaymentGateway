package com.dxvalley.paymentgateway.retailservices.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dxvalley.paymentgateway.retailservices.dao.DeviceConfigDao;
import com.dxvalley.paymentgateway.retailservices.models.DeviceConfig;


@Database(entities = DeviceConfig.class, exportSchema = false, version = 1)
public abstract class DeviceConfigDatabase extends RoomDatabase {
    private static final String DB_NAME = "device_config";
    private static DeviceConfigDatabase instance;

    public static synchronized DeviceConfigDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DeviceConfigDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract DeviceConfigDao configDao();
}
