package com.danilovalerio.mycoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity

class MainActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
