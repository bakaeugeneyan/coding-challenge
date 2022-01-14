package com.example.codingchallenge.Interface

import com.example.codingchallenge.models.UserList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ReqresApi {

    @GET("/api/users")
    suspend fun getUserList(
        @QueryMap searchQuery: Map<String, String>
    ): Response<UserList>
}