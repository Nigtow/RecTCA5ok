package com.example.androidjogo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.example.androidjogo.entidades.CategoriasResponse
import com.example.androidjogo.services.PerguntaService
import kotlinx.android.synthetic.main.fragment_configuracao.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConfiguracaoFragment : Fragment() {

    lateinit var categoriasResponse:CategoriasResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.androidjogo.R.layout.fragment_configuracao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(activity!!.getPreferences(Context.MODE_PRIVATE).getString("email", "null")=="null"){
            Navigation.findNavController(activity!!, R.id.fragment_jogo).navigate(R.id.loginFragment)
        }else{
            val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, arrayOf<String>("Fácil", "Médio", "Difícil"))
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dificuldadeseletor!!.setAdapter(adapter)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(PerguntaService::class.java).categoria().enqueue(object: Callback<CategoriasResponse> {
                override fun onFailure(call: Call<CategoriasResponse>, t: Throwable) {
                }
                override fun onResponse(call: Call<CategoriasResponse>, response: Response<CategoriasResponse>) {
                    categoriasResponse=response.body()!!
                    val adaptercategoria = ArrayAdapter(activity, android.R.layout.simple_spinner_item, response.body()!!.categorias.map { Categoria -> Categoria.name })
                    adaptercategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categoriasseletor!!.setAdapter(adaptercategoria)
                }
            })

            categoriasseletor.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selecionada = categoriasseletor.selectedItem.toString()
                    val indexOf = (categoriasResponse.categorias.map { Categoria -> Categoria.name }.indexOf(selecionada))+9
                    activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString("categoria", indexOf.toString()).apply()
                }
            }

            dificuldadeseletor.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    var dificuldade = ""
                    if(dificuldadeseletor.selectedItem.toString()=="Fácil"){
                        dificuldade="easy"
                    }else if(dificuldadeseletor.selectedItem.toString()=="Médio"){
                        dificuldade="medium"
                    }else{
                        dificuldade="hard"
                    }
                    activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString("dificuldade", dificuldade).apply()
                }
            }
        }
    }
}
