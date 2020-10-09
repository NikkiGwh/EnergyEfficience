package com.example.energyefficience;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MergeSortNumberDao {

    @Query("SELECT * FROM MergeSortNumberEntity ORDER BY id ASC")
   MergeSortNumberEntity[] getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MergeSortNumberEntity> numbers);

    @Query("DELETE FROM MergeSortNumberEntity")
    void deleteAll();

    @Delete
    void delete(MergeSortNumberEntity numberEntity);
}
