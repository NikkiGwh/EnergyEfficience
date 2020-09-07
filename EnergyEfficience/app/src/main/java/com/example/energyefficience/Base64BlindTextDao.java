package com.example.energyefficience;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Base64BlindTextDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Base64BlindTextEntity x);
    @Query("DELETE FROM Base64BlindTextEntity")
    void deleteAll();
    @Query("SELECT * from Base64BlindTextEntity ORDER BY BlindText ASC")
    LiveData<List<Base64BlindTextEntity>> getAllEntries();



}
