package com.danilovalerio.mycoin.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listeners()

    }

    private fun listeners(){
        btnCadastrar.setOnClickListener(){
            startActivity(Intent(this, CadastroActivity::class.java))
        }

//        tvJaTenhoConta.setOnClickListener(){
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
    }
}
