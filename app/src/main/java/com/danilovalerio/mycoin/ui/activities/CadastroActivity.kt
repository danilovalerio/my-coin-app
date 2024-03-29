package com.danilovalerio.mycoin.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.util.*
import com.danilovalerio.mycoin.data.Usuario
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
        val corError = "#FFFC00"

        //Validar campos vazios
        if (nome.isEmpty()) {
            etNome.setError("Este campo não pode ser vazio")
            mudarEditTextHintColor(etNome,corError)
            etEmail.requestFocus()
        }

        if (email.isEmpty()) {
            etEmail.setError("Este campo não pode ser vazio")
            mudarEditTextHintColor(etEmail,corError)
            etEmail.requestFocus()
        }

        if (senha.isEmpty()) {
            etSenha.setError("Este campo não pode ser vazio")
            mudarEditTextHintColor(etSenha,corError)
            etSenha.requestFocus()
        }

        if (!validarEmail(email)) {
            etEmail.setError("E-mail inválido")
            mudarEditTextHintColor(etEmail,corError)
            etEmail.requestFocus()
        }

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    msgShort(this, "Usuário criado com sucesso.\n" + auth.uid.toString())
                    val usuario = Usuario(
                        auth.uid.toString(),
                        nome,
                        email,
                        senha
                    )
                    salvarUsuarioEmailId(usuario)
                    startActivity(Intent(this,
                        PrincipalActivity::class.java))
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
