package com.example.codingchallenge.data.database

import androidx.room.TypeConverter
import com.example.codingchallenge.models.UserList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun itunesToString(itunesList: UserList): String {
        return gson.toJson(itunesList)
    }

    @TypeConverter
    fun stringToItunes(data: String) : UserList {
        val listType = object : TypeToken<UserList>() {}.type
        return gson.fromJson(data, listType)
    }
}