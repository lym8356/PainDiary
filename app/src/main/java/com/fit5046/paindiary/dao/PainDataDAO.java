package com.fit5046.paindiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit5046.paindiary.entity.PainData;

import java.util.Date;
import java.util.List;

@Dao
public interface PainDataDAO {
    @Query("SELECT * FROM PainData ORDER BY recordDate DESC")
    LiveData<List<PainData>> getAll();

    @Query("SELECT * FROM PainData WHERE recordDate = :searchDate LIMIT 1")
    PainData findByDate(String searchDate);

    @Query("SELECT * FROM PainData WHERE pid = :recordID LIMIT 1")
    PainData findByID(int recordID);

//    @Query("SELECT COUNT(*) FROM PainData WHERE painLocation = :searchPainLocation")
//    LiveData<Integer> countPainLocation(String searchPainLocation);

    @Insert
    void insert(PainData painData);

    @Delete
    void delete(PainData painData);

    @Update
    void updatePainData(PainData painData);

    @Query("DELETE FROM PainData")
    void deleteAll();
}
