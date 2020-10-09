package com.example.energyefficience;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MergeSortNumberEntity {
    public MergeSortNumberEntity(int id, int value){
        this.id = id;
        this.value = value;
    }
    @PrimaryKey
    public int id;
    @ColumnInfo(name="value")
    public int value;
}
