package com.example.mostri.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {StoredUser.class}, version = 1)
public abstract class StoredUserRepository extends RoomDatabase {
    public abstract StoredUserDao storedUserDao();

    private static StoredUserRepository instance;

    public static synchronized StoredUserRepository getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            StoredUserRepository.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
