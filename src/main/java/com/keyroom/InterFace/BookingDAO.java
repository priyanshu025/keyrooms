package com.keyroom.InterFace;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.keyroom.Model.BookingMod;

@Dao
public interface BookingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookingMod row);

    @Query("DELETE FROM booking")
    void deleteAll();


    @Query("SELECT status FROM booking WHERE id = :id")
    String getStatusInfo(Integer id);

    @Query("update booking set status= :status where id= :id")
    void updateStatus(String status, Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllList(BookingMod rowList);

    @Query("delete from booking where id = :id")
    int deleteRow(Integer id);
}
