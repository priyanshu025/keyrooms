package com.keyroom.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "booking")
public class BookingMod {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    Integer id;
    @NonNull
    @ColumnInfo(name = "status")
    String status;

    public BookingMod(Integer id, String status){
        this.id=id;
        this.status=status;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

}

