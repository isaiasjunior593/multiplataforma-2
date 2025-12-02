package com.example.gestaoabrigoanimais.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 10.0.2.2 é o IP do localhost do PC visto de dentro do Emulador Android
    private const val BASE_URL = "http://10.0.2.2:5000/" 

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}