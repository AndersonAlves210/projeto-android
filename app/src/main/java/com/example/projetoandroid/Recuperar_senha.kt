package com.example.projetoandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projetoandroid.databinding.ActivityRecuperarSenhaBinding

class Recuperar_senha : ComponentActivity() {

    /*VARIÁVEL QUE VAI RECEBER O BINDING DA ACTIVITY MAIN*/
    private lateinit var binding: ActivityRecuperarSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecuperarSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltarRecuperarSenha.setOnClickListener(){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}