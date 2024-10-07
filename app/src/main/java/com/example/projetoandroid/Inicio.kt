package com.example.projetoandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.projetoandroid.databinding.ActivityTelaInicioBinding

class Inicio : ComponentActivity() {

    /*VARIÁVEL QUE VAI RECEBER O BINDING DA ACTIVITY MAIN*/
    private lateinit var binding: ActivityTelaInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTelaInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}