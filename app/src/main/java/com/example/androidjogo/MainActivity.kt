package com.example.androidjogo

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setupWithNavController(
            Navigation.findNavController(this, R.id.fragment_jogo)
        )

        nav_view.setOnNavigationItemSelectedListener {
            NavigationUI.onNavDestinationSelected(
                it,
                Navigation.findNavController(this, R.id.fragment_jogo)
            )
        }
    }
}
