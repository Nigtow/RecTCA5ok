package com.example.androidjogo.entidades

import com.google.gson.annotations.SerializedName

data class Perguntas(
    @SerializedName("response_code")
    var codigo: Int,
    @SerializedName("results")
    var perguntas: List<Pergunta>
)