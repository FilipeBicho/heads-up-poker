package com.example.poker.bot

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatgptApiInterface {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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",
    )
    @POST("chat/completions")
    fun getChatCompletion(@Body request: ChatRequest): Call<ChatResponse>

}