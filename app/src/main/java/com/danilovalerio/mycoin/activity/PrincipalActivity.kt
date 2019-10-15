package com.danilovalerio.mycoin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.msgShort
import com.google.android.material.snackbar.Snackbar
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
            msgShort(this,"Cadastro de despesa")
        }

        menu_receita.setOnClickListener(){
            msgShort(this,"Cadastro de receita")
        }
    }

}
