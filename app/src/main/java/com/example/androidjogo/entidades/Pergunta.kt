package com.example.androidjogo.entidades

import com.google.gson.annotations.SerializedName

data class Pergunta(
    @SerializedName("category")
    var categoria: String,
    @SerializedName("correct_answer")
    var respostaCerta: String,
    @SerializedName("difficulty")
    var dificuldade: String,
    @SerializedName("incorrect_answers")
    var respostasErradas: ArrayList<String>,
    @SerializedName("type")
    var tipo: String,
    @SerializedName("question")
    var questao: String?
)