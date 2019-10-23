package com.danilovalerio.mycoin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.graphics.toColorInt
import com.danilovalerio.mycoin.*
import com.danilovalerio.mycoin.util.etToString
import com.danilovalerio.mycoin.util.msgShort
import com.danilovalerio.mycoin.util.validarEmail
import com.danilovalerio.mycoin.util.validarStr
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        listeners()

        if(usuarioLogado()){
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }

        etEmail.requestFocus()
    }

    private fun listeners(){
        tvCriarConta.setOnClickListener(){
            startActivity(Intent(this,
                CadastroActivity::class.java))
        }

        btnAcessar.setOnClickListener(){
            logar(
                etToString(etEmail),
                etToString(etSenha)
            )
        }
    }

    private fun logar(email:String, senha:String) {

        try {
            val corError = "#FFFC00"

            if (!validarStr(email)) {
                etEmail.setError("Preencha este campo.")
                etEmail.setHintTextColor(corError.toColorInt())
                etEmail.requestFocus()
            }

            if (!validarEmail(email)) {
                etEmail.setError("E-mail inválido.")
                etEmail.setHintTextColor(corError.toColorInt())
                etEmail.requestFocus()
            }

            if (!validarStr(senha)) {
                etSenha.setError("Preencha este campo.")
                etSenha.setHintTextColor(corError.toColorInt())
                etEmail.requestFocus()
            }

            if (!validarEmail(email) || !validarStr(email) || !validarStr(senha)) {
                return
            }

            auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //val user = auth.currentUser.toString()
                        msgShort(this, "Login efetuado")
                        startActivity(Intent(this, PrincipalActivity::class.java))
                    } else {
                        Toast.makeText(
                            baseContext, "Autenticação falhou.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            msgShort(this, e.toString())
        }
    }

    private fun usuarioLogado() : Boolean {
        val usuario = auth.currentUser?.uid.toString()
        if(usuario.isNullOrEmpty() || usuario == "null"){
            return false
        }

        return true
    }
}
