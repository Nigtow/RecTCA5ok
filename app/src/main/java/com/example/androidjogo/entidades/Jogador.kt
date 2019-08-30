package com.example.androidjogo.entidades

import java.util.*

class Jogador(
    var email: String,
    var nome: String,
    var partidasJogadas: Int,
    var pontuacao: Int,
    var sucesso: Boolean,
    var ultimaPartida: Date
)