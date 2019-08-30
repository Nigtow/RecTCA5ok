package com.example.androidjogo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidifpr.quiz.ui.RankingAdapter
import com.example.androidjogo.entidades.JogadoresResponse
import com.example.androidjogo.services.JogadorService
import kotlinx.android.synthetic.main.fragment_ranking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RankingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://tads2019-todo-list.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(JogadorService::class.java).ranking().enqueue(object : Callback<JogadoresResponse> {
            override fun onFailure(call: Call<JogadoresResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<JogadoresResponse>, response: Response<JogadoresResponse>) {
                val adapter = RankingAdapter(response.body()!!.ranking)
                ranking.adapter = adapter
                ranking.layoutManager = LinearLayoutManager(activity)
            }
        })
    }
}
