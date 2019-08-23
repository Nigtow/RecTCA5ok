package com.example.androidjogo.entidades

import com.google.gson.annotations.SerializedName

data class Categoria(
    var id: String,
    @SerializedName("name")
    var nome: String
) {
    override fun toString(): String {
        return nome
    }
}