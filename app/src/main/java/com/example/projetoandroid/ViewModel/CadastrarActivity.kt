package com.example.projetoandroid.ViewModel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.projetoandroid.databinding.ActivityCadastrarBinding
import com.google.firebase.auth.FirebaseAuth

class CadastrarActivity : ComponentActivity() {

    private lateinit var binding: ActivityCadastrarBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnCadastrarVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCadastrarCadastreSe.setOnClickListener {
            val email = binding.txtEmailCadastreSe.text.toString().trim()
            val senha = binding.txtSenhaCadastreSe.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                if (email.isEmpty()) {
                    binding.txtEmailCadastreSe.error = "Preencha o campo"
                }
                if (senha.isEmpty()) {
                    binding.txtSenhaCadastreSe.error = "Preencha o campo"
                }

            } else {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("CadastrarActivity", "Cadastro realizado com sucesso")
                        } else {
                            Log.e(
                                "CadastrarActivity",
                                "Erro ao realizar cadastro: ${task.exception?.message}"
                            )
                            Toast.makeText(
                                this@CadastrarActivity,
                                "Erro ao realizar cadastro",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}

