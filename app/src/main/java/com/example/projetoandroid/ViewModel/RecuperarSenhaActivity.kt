package com.example.projetoandroid.ViewModel

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.projetoandroid.databinding.ActivityRecuperarSenhaBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarSenhaActivity : ComponentActivity() {

    private lateinit var binding: ActivityRecuperarSenhaBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecuperarSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnVoltarRecuperarSenha.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRecuperarSenha.setOnClickListener {
            val email = binding.txtEmailRecuperarSenha.text.toString().trim()
            if (!TextUtils.isEmpty(email)) {
                ResetPassword(email)
            } else {
                binding.txtEmailRecuperarSenha.error = "Campo obrigatório"
            }
        }
    }

    private fun ResetPassword(email: String) {
        binding.btnRecuperarSenha.visibility = View.INVISIBLE

        mAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this, "Verifique seu Email", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error :- ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnRecuperarSenha.visibility = View.VISIBLE
            }
    }
}
