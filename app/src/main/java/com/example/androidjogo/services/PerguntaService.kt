package com.example.androidjogo.services

import com.example.androidjogo.entidades.CategoriasResponse
import com.example.androidjogo.entidades.Perguntas
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PerguntaService {
    @Headers("Accept: application/json")
    @GET("api.php")
    fun pergunta(
        @Query("amount")
        amount: String,
        @Query("category")
        category: String,
        @Query("difficulty")
        difficulty: String
    ): Call<Perguntas>

    @Headers("Accept: application/json")
    @GET("api_category.php")
    fun categoria(
    ): Call<CategoriasResponse>
}