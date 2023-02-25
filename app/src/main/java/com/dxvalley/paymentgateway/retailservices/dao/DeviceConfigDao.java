package com.dxvalley.paymentgateway.retailservices.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.dxvalley.paymentgateway.retailservices.models.DeviceConfig;

import java.util.List;
@Dao
public interface DeviceConfigDao {
    @Query("SELECT * FROM device_config")
    List<DeviceConfig> getConfigList();
    @Insert
    void insertConfig(DeviceConfig config);
    @Update
    void updateConfig(DeviceConfig config);
    @Delete
    void deleteConfig(DeviceConfig config);
}
