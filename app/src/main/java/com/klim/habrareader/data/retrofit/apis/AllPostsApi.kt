package com.klim.habrareader.data.retrofit.apis

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AllPostsApi {

    @GET("all/{rating}/")
    fun allPosts(@Path(value = "rating", encoded = true) rating: String): Call<String>

    @GET("top/{period}/")
    fun topPosts(@Path(value = "period", encoded = true) period: String): Call<String>

}