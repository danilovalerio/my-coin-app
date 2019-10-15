package com.danilovalerio.mycoin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.danilovalerio.mycoin.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        listeners()
    }

    private fun listeners(){
        tvCriarConta.setOnClickListener(){
            startActivity(Intent(this,CadastroActivity::class.java))
        }

        btnAcessar.setOnClickListener(){
            logar(etToString(etEmail), etToString(etSenha))
//            startActivity(Intent(this,CadastroActivity::class.java))
        }
    }

    private fun logar(email:String, senha:String){

        if(!validarEmail(email)){
            etEmail.setError("E-mail inválido.")
        }

        if(!validarStr(email)){
            etEmail.setError("Preencha este campo.")
        }

        if(!validarStr(senha)){
            etSenha.setError("Preencha este campo.")
        }

        if(!validarEmail(email) || !validarStr(email) || !validarStr(senha)){
            return
        }

        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    msgShort(this, "Login efetuado")
                    startActivity(Intent(this, PrincipalActivity::class.java))

                } else {
                    Toast.makeText(baseContext, "Autenticação falhou.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}
