package com.example.androidjogo


import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.androidjogo.entidades.Jogador
import com.example.androidjogo.entidades.Pergunta
import com.example.androidjogo.entidades.Perguntas
import com.example.androidjogo.services.JogadorService
import com.example.androidjogo.services.PerguntaService
import kotlinx.android.synthetic.main.fragment_pergunta.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PerguntaFragment : Fragment() {
    lateinit var service: PerguntaService
    lateinit var serviceJogador: JogadorService
    lateinit var pergunta: Pergunta
    lateinit var timer:CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pergunta, container, false)
    }

    override fun onDestroyView() {
        timer.cancel()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", "null")=="null"){
            timer = object: CountDownTimer(20000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tempo.text = (millisUntilFinished / 1000).toString()
                }

                override fun onFinish() {
                }
            }
            Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.loginFragment)
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            service = retrofit.create(PerguntaService::class.java)

            val retrofitJogador = Retrofit.Builder()
                .baseUrl("https://tads2019-todo-list.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            serviceJogador = retrofitJogador.create<JogadorService>(JogadorService::class.java)

            service.pergunta("1", activity!!.getPreferences(Context.MODE_PRIVATE).getString("categoria", ""), activity!!.getPreferences(Context.MODE_PRIVATE).getString("dificuldade", "")).enqueue(object : Callback<Perguntas> {
                override fun onFailure(call: Call<Perguntas>, t: Throwable) {}
                override fun onResponse(call: Call<Perguntas>, response: Response<Perguntas>){
                    pergunta = response.body()!!.results[0]

                    val respostas = ArrayList<String>()
                    var i = 0;

                    respostas.add(pergunta.correct_answer)
                    while(i<pergunta.incorrect_answers.size){
                        respostas.add(pergunta.incorrect_answers[i])
                        i++
                    }
                    respostas.shuffle()

                    titulo.text = pergunta.question

                    if(respostas.size==1){
                        resposta1.text = respostas[0]
                        resposta2.visibility = View.INVISIBLE
                        resposta3.visibility = View.INVISIBLE
                        resposta4.visibility = View.INVISIBLE
                    }else if(respostas.size==2){
                        resposta1.text = respostas[0]
                        resposta2.text = respostas[1]
                        resposta3.visibility = View.INVISIBLE
                        resposta4.visibility = View.INVISIBLE
                    }else if(respostas.size==3){
                        resposta1.text = respostas[0]
                        resposta2.text = respostas[1]
                        resposta3.text = respostas[2]
                        resposta4.visibility = View.INVISIBLE
                    }else if(respostas.size==4){
                        resposta1.text = respostas[0]
                        resposta2.text = respostas[1]
                        resposta3.text = respostas[2]
                        resposta4.text = respostas[3]
                    }

                    timer = object: CountDownTimer(20000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            tempo.text = (millisUntilFinished / 1000).toString()
                        }

                        override fun onFinish() {
                        }
                    }
                    timer.start()
                }
            })

            var pontos = 0
            if(activity!!.getPreferences(Context.MODE_PRIVATE).getString("dificuldade", "easy")=="easy"){
                pontos = 5
            }else if(activity!!.getPreferences(Context.MODE_PRIVATE).getString("dificuldade", "easy")=="medium"){
                pontos = 8
            }else if(activity!!.getPreferences(Context.MODE_PRIVATE).getString("dificuldade", "easy")=="hard"){
                pontos = 10
            }

            resposta1.setOnClickListener{
                if(pergunta.correct_answer==resposta1.text.toString()){
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                        }

                    })
                    Toast.makeText(activity, "Certa", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }else{
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos*-1).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                        }

                    })
                    Toast.makeText(activity, "Errada", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }
            }
            resposta2.setOnClickListener{
                if(pergunta.correct_answer==resposta2.text.toString()){
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                    Toast.makeText(activity, "Certa", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }else{
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos*-1).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                    Toast.makeText(activity, "Errada", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }
            }
            resposta3.setOnClickListener{
                if(pergunta.correct_answer==resposta3.text.toString()){
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                    Toast.makeText(activity, "Certa", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }else{
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos*-1).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                    Toast.makeText(activity, "Errada", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }
            }
            resposta4.setOnClickListener{
                if(pergunta.correct_answer==resposta4.text.toString()){
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                    Toast.makeText(activity, "Certa", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }else{
                    serviceJogador.pontuacao(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", ""),
                        pontos*-1).enqueue(object: Callback<Jogador>{
                        override fun onFailure(call: Call<Jogador>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                    Toast.makeText(activity, "Errada", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
                }
            }
        }
    }


}
