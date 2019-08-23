package com.example.androidjogo


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.androidjogo.entidades.Jogador
import com.example.androidjogo.services.JogadorService
import kotlinx.android.synthetic.main.fragment_cadastro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CadastroFragment : Fragment() {
    lateinit var services: JogadorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString("email", "null").apply()
        activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString("senha", "null").apply()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://tads2019-todo-list.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        services = retrofit.create<JogadorService>(JogadorService::class.java)

        cadastrar2.setOnClickListener {
            services.registrar(txtnome.text.toString(), txtemail2.text.toString(), txtsenha2.text.toString())
                .enqueue(object : Callback<Jogador> {
                    override fun onFailure(call: Call<Jogador>, t: Throwable) {}
                    override fun onResponse(call: Call<Jogador>, response: Response<Jogador>) {
                        if (response.body()!!.sucesso) {
                            Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.loginFragment)
                        }
                    }
                })
        }

        login2.setOnClickListener {
            Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.loginFragment)
        }
    }
}
