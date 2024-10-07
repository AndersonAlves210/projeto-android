package com.example.projetoandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projetoandroid.databinding.ActivityMainBinding
import com.example.projetoandroid.ui.theme.ProjetoAndroidTheme

class MainActivity : ComponentActivity() {
    /*VARIÁVEL QUE VAI RECEBER O BINDING DA ACTIVITY MAIN*/
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.btnCadastrar.setOnClickListener(){
            val intent = Intent(this, Cadastrar::class.java)
            startActivity(intent)
        }

    }
}

