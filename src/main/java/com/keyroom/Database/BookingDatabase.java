package com.keyroom.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.keyroom.InterFace.BookingDAO;
import com.keyroom.Model.BookingMod;


@Database(entities = {BookingMod.class}, version = 2)
public abstract class BookingDatabase extends RoomDatabase {

    public abstract BookingDAO bookingDAO();
    private static BookingDatabase INSTANCE;

    public static BookingDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookingDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookingDatabase.class, "booking_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
