package com.example.poker.bot

import com.example.poker.BuildConfig
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatgptApiInterface {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.API_KEY}",
    )
    @POST("chat/completions")
    fun getChatCompletion(@Body request: ChatRequest): Call<ChatResponse>
}