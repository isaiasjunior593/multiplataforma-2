package com.example.gestaoabrigoanimais.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/animais")
    suspend fun salvarAnimal(@Body animal: AnimalRequest): Response<Any>
}