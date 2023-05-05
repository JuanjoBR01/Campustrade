package com.example.campustrade.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.campustrade.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ")
    suspend fun gerAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}