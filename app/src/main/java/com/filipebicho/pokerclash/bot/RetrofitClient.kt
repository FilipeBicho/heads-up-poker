package com.filipebicho.pokerclash.bot

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.openai.com/v1/"

    fun getOpenAiClient(): ChatgptApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatgptApiInterface::class.java)
    }
}