package com.danilovalerio.mycoin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.etToString
import com.danilovalerio.mycoin.msgShort
import com.danilovalerio.mycoin.validarEmail
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        auth = FirebaseAuth.getInstance()
        listeners()

    }

    private fun listeners() {
        btnCriarConta.setOnClickListener() {
            val nome = etNome.text.toString()
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()

            criarLogin(nome, email, senha)
        }
    }

    private fun criarLogin(nome: String, email: String, senha: String) {
        //Campos vazios
        if (nome.isEmpty()) {
            etNome.setError("Este campo não pode ser vazio")
        }

        if (email.isEmpty()) {
            etEmail.setError("Este campo não pode ser vazio")
        }

        if (senha.isEmpty()) {
            etSenha.setError("Este campo não pode ser vazio")
        }

        if (!validarEmail(email)) {
            etEmail.setError("E-mail inválido")
        }

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    msgShort(this, "Criação do Usuário com e-mail: success.")
                } else {
                    Toast.makeText(
                        baseContext, "Criação do Usuário com e-mail: failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
