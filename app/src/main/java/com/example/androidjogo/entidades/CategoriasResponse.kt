package com.example.androidjogo.entidades

import com.google.gson.annotations.SerializedName

data class CategoriasResponse (
    @SerializedName("trivia_categories")
    var categorias: List<Categoria>
)