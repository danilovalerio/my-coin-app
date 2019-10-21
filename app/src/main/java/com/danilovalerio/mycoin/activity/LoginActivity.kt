package com.danilovalerio.mycoin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.graphics.toColorFilter
import androidx.core.graphics.toColorInt
import com.danilovalerio.mycoin.*
import com.danilovalerio.mycoin.helper.etToString
import com.danilovalerio.mycoin.helper.msgShort
import com.danilovalerio.mycoin.helper.validarEmail
import com.danilovalerio.mycoin.helper.validarStr
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
    }

    private fun listeners(){
        tvCriarConta.setOnClickListener(){
            startActivity(Intent(this,CadastroActivity::class.java))
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
            if (!validarEmail(email)) {
                etEmail.setError("E-mail inválido.")
                etEmail.setHintTextColor(corError.toColorInt())
            }

            if (!validarStr(email)) {
                etEmail.setError("Preencha este campo.")
                etEmail.setHintTextColor(corError.toColorInt())
            }

            if (!validarStr(senha)) {
                etSenha.setError("Preencha este campo.")
                etEmail.setHintTextColor(corError.toColorInt())
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
