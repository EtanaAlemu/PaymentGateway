package com.dxvalley.paymentgateway.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tip_db")
public class Tip {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "isByPercent")
    boolean isByPercent;
    @ColumnInfo(name = "value")
    float value;

    public Tip(boolean isByPercent, float value) {
        this.isByPercent = isByPercent;
        this.value = value;
    }

    @Ignore
    public Tip(int id, boolean isByPercent, float value) {
        this.id = id;
        this.isByPercent = isByPercent;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isByPercent() {
        return isByPercent;
    }

    public void setByPercent(boolean byPercent) {
        isByPercent = byPercent;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
