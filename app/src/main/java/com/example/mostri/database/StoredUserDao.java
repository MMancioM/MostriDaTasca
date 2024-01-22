package com.example.mostri.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;

@Dao
public interface StoredUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertAll(StoredUser... storedUsers);

    @Query("SELECT * FROM storeduser WHERE uid = :uid")
    public ListenableFuture<StoredUser> getUser(String uid);
}
