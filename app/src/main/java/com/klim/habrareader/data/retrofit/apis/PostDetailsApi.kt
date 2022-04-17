package com.klim.habrareader.data.retrofit.apis

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostDetailsApi {

    @GET("post/{post_id}/")
    fun postDetails(@Path(value = "post_id", encoded = true) id: Int): Call<String>

    @GET("post/{post_id}/")
    suspend fun postDetails_S(@Path(value = "post_id", encoded = true) id: Int): String

}