package com.example.codingchallenge.data

import com.example.codingchallenge.Interface.ReqresApi
import com.example.codingchallenge.models.UserList
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val reqresApi: ReqresApi
) {

    suspend fun getUserList(queries: Map<String, String>): Response<UserList> {
        return reqresApi.getUserList(queries)
    }
}