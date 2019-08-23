package com.example.androidjogo.services

import com.example.androidjogo.entidades.Jogador
import com.example.androidjogo.entidades.Jogadores
import retrofit2.Call
import retrofit2.http.*

interface JogadorService {

    @Headers("Accept: application/json")
    @FormUrlEncoded

    @POST("usuario/login")
    fun login(
        @Field("email")
        email:String,
        @Field("senha")
        password: String
    ): Call<Jogador>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("usuario/registrar")
    fun registrar(
        @Field("nome")
        nome:String,
        @Field("email")
        email:String,
        @Field("senha")
        password: String
    ): Call<Jogador>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("usuario/pontuacao")
    fun pontuacao(
        @Field("email")
        email:String,
        @Field("senha")
        senha:String,
        @Field("pontos")
        pontos:Int
    ): Call<Jogador>

    @Headers("Accept: application/json")
    @GET("ranking")
    fun ranking(
    ): Call<Jogadores>
}