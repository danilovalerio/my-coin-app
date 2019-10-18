package com.danilovalerio.mycoin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.config.ConfiguracaoFirebase
import com.danilovalerio.mycoin.helper.*
import com.danilovalerio.mycoin.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        supportActionBar?.title = "Cadastro"

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().getReference()

        listeners()

    }

    private fun listeners() {
        btnCriarConta.setOnClickListener() {
            criarLogin(
                etToString(etNome),
                etToString(etEmail),
                etToString(etSenha)
            )
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
                    msgShort(this, "Usuário criado com sucesso.\n" + auth.uid.toString())
                    val usuario = Usuario(auth.uid.toString(), nome, email, senha)
                    salvarUsuarioEmailId(usuario)
                    startActivity(Intent(this,PrincipalActivity::class.java))
                    finish()
                } else {
                    msgShort(
                        this,
                        "Falha na criação do usuário\n" + task.exception!!.message.toString()
                    )
                }
            }
    }

    private fun salvarUsuarioEmailId(usuario: Usuario) {
        val email = usuario.email
        val codificado = codificarBase64(email)
        firebase.child("usuarios")
            .child(codificado)
            .setValue(usuario)
    }
}
