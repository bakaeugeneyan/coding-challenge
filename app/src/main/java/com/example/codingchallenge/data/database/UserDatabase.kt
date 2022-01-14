package com.example.codingchallenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(UserTypeConverter::class)
abstract class UserDatabase : RoomDatabase(){

    abstract fun userDAo(): UserDao
}