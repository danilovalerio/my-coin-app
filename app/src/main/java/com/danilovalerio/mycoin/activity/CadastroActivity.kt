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
            criarLogin(etToString(etNome), etToString(etEmail), etToString(etSenha))
        }
    }

    private fun criarLogin(nome: String, email: String, senha: String) {
        //Validar campos vazios
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
                    msgShort(this, "Usuário criado com sucesso.")
                } else {
                    msgShort(this,"Falha na criação do usuário\n"+task.exception!!.message.toString())
                }
            }
    }
}
