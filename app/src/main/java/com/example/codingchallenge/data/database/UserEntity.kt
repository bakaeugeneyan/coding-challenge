package com.example.codingchallenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.codingchallenge.models.UserList
import com.example.codingchallenge.utils.Constants.Companion.USER_TABLE

@Entity(tableName = USER_TABLE)
class UserEntity(
    var userList: UserList
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}