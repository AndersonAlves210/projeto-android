package com.example.projetoandroid.ViewModel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.projetoandroid.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnEntrar.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val senha = binding.txtSenha.text.toString().trim()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnSuccessListener {
                        val intent = Intent(this, InicioActivity::class.java)
                        startActivity(intent)
                        finish()
                    }.addOnFailureListener {
                        binding.txtEmail.error = "Email ou senha incorretos"
                    }
            } else {
                if (email.isEmpty()) {
                    binding.txtEmail.error = "Por favor, preencha o email"
                }
                if (senha.isEmpty()) {
                    binding.txtSenha.error = "Por favor, preencha a senha"
                }
            }
        }
        binding.txtRecuperarSenha.setOnClickListener {
            val intent = Intent(this, RecuperarSenhaActivity::class.java)
            startActivity(intent)
        }
    }
}
