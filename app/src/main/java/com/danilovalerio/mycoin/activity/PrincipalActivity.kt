package com.danilovalerio.mycoin.activity

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.helper.msgShort
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        setSupportActionBar(toolbar)

//        menu.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        listeners()
    }

    private fun listeners(){
        menu_despesa.setOnClickListener(){
            msgShort(this, "Cadastro de despesa")
            startActivity(Intent(this, DespesasActivity::class.java))
        }

        menu_receita.setOnClickListener(){
            msgShort(this, "Cadastro de receita")
            startActivity(Intent(this, ReceitasActivity::class.java))
        }
    }

}
