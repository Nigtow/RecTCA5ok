package com.androidifpr.quiz.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.androidjogo.R
import com.example.androidjogo.entidades.Jogador
import com.example.androidjogo.entidades.Pergunta
import com.example.androidjogo.services.JogadorService
import com.example.androidjogo.services.PerguntaListener
import kotlinx.android.synthetic.main.resposta_alternativa.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlternativaAdapter(private var alternativas: List<String>, private var pergunta: Pergunta, private var listener: PerguntaListener) :
    RecyclerView.Adapter<AlternativaAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlternativaAdapter.ResultViewHolder =
        ResultViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.resposta_alternativa, parent, false)
        )

    override fun getItemCount(): Int = alternativas.size

    override fun onBindViewHolder(holder: AlternativaAdapter.ResultViewHolder, position: Int) =
        holder.preencherView(alternativas[position])

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun preencherView(resposta:String) {
            val retrofitJogador = Retrofit.Builder()
                .baseUrl("https://tads2019-todo-list.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val serviceJogador = retrofitJogador.create<JogadorService>(JogadorService::class.java)

            itemView.alternativa.text=resposta
            itemView.alternativa.setOnClickListener(){
                var pontos=0
                if(pergunta.dificuldade=="hard"){
                    pontos=10
                }else if(pergunta.dificuldade=="medium"){
                    pontos=8
                }else{
                    pontos=5
                }

                if(pergunta.resposta_certa==itemView.alternativa.text.toString()){
                    serviceJogador.pontuacao(listener.getEmail(), listener.getSenha(), pontos).enqueue(object: Callback<Jogador> {
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            Toast.makeText(listener.getPerguntaActivity(), "Certa = + "+pontos+" pontos", Toast.LENGTH_SHORT).show()
                            listener.sair()
                        }
                    })
                }else{
                    serviceJogador.pontuacao(listener.getEmail(), listener.getSenha(), pontos*-1).enqueue(object: Callback<Jogador> {
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            Toast.makeText(listener.getPerguntaActivity(), "Errada = - "+pontos+" pontos", Toast.LENGTH_SHORT).show()
                            listener.sair()
                        }
                    })
                }
            }
        }
    }

}