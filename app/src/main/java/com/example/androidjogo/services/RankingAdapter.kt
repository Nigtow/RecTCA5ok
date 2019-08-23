package com.androidifpr.quiz.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidjogo.R
import com.example.androidjogo.entidades.Jogador
import kotlinx.android.synthetic.main.list_jogador.view.*
import java.text.DateFormat

class RankingAdapter(private var jogadores: List<Jogador>) :
    RecyclerView.Adapter<RankingAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingAdapter.ResultViewHolder =
        ResultViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_jogador, parent, false)
        )

    override fun getItemCount(): Int = jogadores.size

    override fun onBindViewHolder(holder: RankingAdapter.ResultViewHolder, position: Int) =
        holder.preencherView(jogadores[position])

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun preencherView(jogador: Jogador) {
            itemView.jogador.text = jogador.nome
            itemView.pontos.text = jogador.pontuacao.toString()
            itemView.ultimaPartida.text = DateFormat.getDateInstance().format(jogador.ultimaPartida)
        }
    }

}