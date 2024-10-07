package com.example.projetoandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.projetoandroid.databinding.ActivityLoginBinding

class Login : ComponentActivity() {

    /*VARIÁVEL QUE VAI RECEBER O BINDING DA ACTIVITY MAIN*/
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnEntrar.setOnClickListener(){
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
        }

        binding.txtRecuperarSenha.setOnClickListener(){
            val intent = Intent(this, Recuperar_senha::class.java)
            startActivity(intent)
        }
    }
}