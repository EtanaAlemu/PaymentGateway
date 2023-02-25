package com.dxvalley.paymentgateway.retailservices.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "device_config")
public class DeviceConfig {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo
    String clientId;
    @ColumnInfo
    String secretId;
    @ColumnInfo
    String apiKey;
    @ColumnInfo
    String deviceId;

    @Ignore
    public DeviceConfig() {
    }

    @Ignore
    public DeviceConfig(int id, String clientId, String secretId, String apiKey, String deviceId) {
        this.id = id;
        this.clientId = clientId;
        this.secretId = secretId;
        this.apiKey = apiKey;
        this.deviceId = deviceId;
    }
    public DeviceConfig( String clientId, String secretId, String apiKey, String deviceId) {
        this.clientId = clientId;
        this.secretId = secretId;
        this.apiKey = apiKey;
        this.deviceId = deviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
