package com.example.codingchallenge.data

import com.example.codingchallenge.data.database.UserDao
import com.example.codingchallenge.data.database.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val userDao: UserDao
) {

    fun readDatabase(): Flow<List<UserEntity>> {
        return userDao.readUsers()
    }

    suspend fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }
}