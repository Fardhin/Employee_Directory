package com.example.employeedirectory.network

import com.example.employeedirectory.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/users?page=2")
    fun getUsers(): Call<UserResponse>
}
