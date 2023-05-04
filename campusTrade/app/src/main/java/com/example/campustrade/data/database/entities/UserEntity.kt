package com.example.campustrade.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.campustrade.dtos.UserObj

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "lastaccess") val lastaccess: String

)

fun UserObj.toDatabase() = UserEntity(name = name, email = email, tag = tag, image = image, lastaccess = lastaccess)