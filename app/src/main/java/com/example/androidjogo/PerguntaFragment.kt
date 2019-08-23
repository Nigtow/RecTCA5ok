package com.example.androidjogo


import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidifpr.quiz.ui.AlternativaAdapter
import com.example.androidjogo.entidades.Jogador
import com.example.androidjogo.entidades.Pergunta
import com.example.androidjogo.entidades.Perguntas
import com.example.androidjogo.services.JogadorService
import com.example.androidjogo.services.PerguntaListener
import com.example.androidjogo.services.PerguntaService
import kotlinx.android.synthetic.main.fragment_pergunta.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PerguntaFragment : Fragment(), PerguntaListener{

    override fun getPerguntaActivity(): MainActivity {
        return activity as MainActivity
    }

    override fun getEmail(): String {
        return activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", "null")
    }

    override fun getSenha(): String {
        return activity!!.getPreferences(Context.MODE_PRIVATE).getString("senha", "null")
    }

    override fun sair() {
        Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.configuracaoFragment)
    }

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

            val dificuldade = activity!!.getPreferences(Context.MODE_PRIVATE).getString("dificuldade", "null")


            service.pergunta("1", activity!!.getPreferences(Context.MODE_PRIVATE).getString("categoria", ""), activity!!.getPreferences(Context.MODE_PRIVATE).getString("dificuldade", "")).enqueue(object : Callback<Perguntas> {
                override fun onFailure(call: Call<Perguntas>, t: Throwable) {}
                override fun onResponse(call: Call<Perguntas>, response: Response<Perguntas>){
                    pergunta = response.body()!!.perguntas[0]

                    val respostas = ArrayList<String>()
                    var i = 0;
                    var pontos=0
                    var segundos:Long=0

                    respostas.add(pergunta.resposta_certa)
                    while(i<pergunta.respostas_erradas.size){
                        respostas.add(pergunta.respostas_erradas[i])
                        i++
                    }
                    respostas.shuffle()

                    titulo.text = pergunta.questao

                    if(pergunta.dificuldade=="hard"){
                        segundos=15000
                        pontos=-10
                    }else if(pergunta.dificuldade=="medium"){
                        segundos=30000
                        pontos=-8
                    }else{
                        segundos=45000
                        pontos=-5
                    }

                    timer = object: CountDownTimer(segundos, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            tempo.text = (millisUntilFinished / 1000).toString()
                        }

                        override fun onFinish() {
                            serviceJogador.pontuacao(getEmail(), getSenha(), pontos).enqueue(object: Callback<Jogador> {
                                override fun onFailure(call: Call<Jogador>, t: Throwable) {
                                }

                                override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                                    Toast.makeText(activity, "Errada = - "+pontos+" pontos", Toast.LENGTH_SHORT).show()
                                    sair()
                                }
                            })
                        }
                    }
                    timer.start()

                    val adapter = AlternativaAdapter(respostas, pergunta, this@PerguntaFragment)
                    listalternativas.adapter = adapter
                    listalternativas.layoutManager = LinearLayoutManager(activity)
                }
            })
        }
    }


}
