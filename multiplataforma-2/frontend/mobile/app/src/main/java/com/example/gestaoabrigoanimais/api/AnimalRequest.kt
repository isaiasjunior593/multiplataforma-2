package com.example.gestaoabrigoanimais.api

data class AnimalRequest(
    val nome: String,
    val raca: String,
    val genero: String,
    val castrado: Boolean,
    val obs: String
)