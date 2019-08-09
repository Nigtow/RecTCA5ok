package com.example.androidjogo.entidades

data class Pergunta(
    var category: String,
    var correct_answer: String,
    var difficulty: String,
    var incorrect_answers: ArrayList<String>,
    var type: String,
    var question: String?
)