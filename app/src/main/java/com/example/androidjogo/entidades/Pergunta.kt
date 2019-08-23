package com.example.androidjogo.entidades

import com.google.gson.annotations.SerializedName

data class Pergunta(
    @SerializedName("category")
    var categoria: String,
    @SerializedName("correct_answer")
    var resposta_certa: String,
    @SerializedName("difficulty")
    var dificuldade: String,
    @SerializedName("incorrect_answers")
    var respostas_erradas: ArrayList<String>,
    @SerializedName("type")
    var tipo: String,
    @SerializedName("question")
    var questao: String?
)