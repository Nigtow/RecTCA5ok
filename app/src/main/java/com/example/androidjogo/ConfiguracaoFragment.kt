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
import com.example.androidjogo.entidades.Categoria
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
            val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, arrayOf<String>(resources.getString(R.string.aleatorio),resources.getString(R.string.facil),resources.getString(R.string.medio),resources.getString(R.string.dificil)))
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dificuldadeSeletor!!.setAdapter(adapter)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(PerguntaService::class.java).categoria().enqueue(object: Callback<CategoriasResponse> {
                override fun onFailure(call: Call<CategoriasResponse>, t: Throwable) {
                }
                override fun onResponse(call: Call<CategoriasResponse>, response: Response<CategoriasResponse>) {
                    categoriasResponse=response.body()!!
                    val categoriaAleatoria = Categoria("", "Random")
                    val adaptercategoria = ArrayAdapter(activity, android.R.layout.simple_spinner_item, (response.body()!!.categorias+categoriaAleatoria))
                    adaptercategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categoriasSeletor!!.setAdapter(adaptercategoria)
                }
            })

            categoriasSeletor.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val escolhida:Categoria = categoriasSeletor.selectedItem as Categoria
                    activity!!.getPreferences(Context.MODE_PRIVATE).edit().putString("categoria", escolhida.id).apply()
                }
            }

            dificuldadeSeletor.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    var dificuldade = ""
                    if(dificuldadeSeletor.selectedItemPosition==0){
                        dificuldade=""
                    }else if(dificuldadeSeletor.selectedItemPosition==1){
                        dificuldade="easy"
                    }else if(dificuldadeSeletor.selectedItemPosition==2){
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
