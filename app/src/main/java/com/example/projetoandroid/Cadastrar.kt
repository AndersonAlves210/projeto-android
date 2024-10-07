package com.example.projetoandroid

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projetoandroid.databinding.ActivityCadastrarBinding
import com.example.projetoandroid.databinding.ActivityLoginBinding

class Cadastrar : ComponentActivity() {

    private lateinit var binding: ActivityCadastrarBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtCepCadastreSe.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val mask = "#####-###"
            private val regex = Regex("[^0-9]") // Only digits allowed

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                val unmasked = s.toString().replace(regex, "") // Remove any non-digit characters

                if (unmasked.length <= 8) {
                    isUpdating = true

                    // Add mask formatting
                    val masked = StringBuilder()
                    var i = 0
                    for (char in mask.toCharArray()) {
                        if (char != '#') {
                            masked.append(char)
                        } else {
                            if (i < unmasked.length) {
                                masked.append(unmasked[i])
                                i++
                            }
                        }
                    }

                    binding.txtCepCadastreSe.setText(masked.toString())
                    binding.txtCepCadastreSe.setSelection(masked.length) // Move cursor to the end

                    isUpdating = false
                }
            }
        })
        binding.btnCadastrarVoltar.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}