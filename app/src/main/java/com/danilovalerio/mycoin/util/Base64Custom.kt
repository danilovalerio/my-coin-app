package com.danilovalerio.mycoin.util

import android.util.Base64


    fun codificarBase64(txt:String):String{
        return Base64.encodeToString(txt.toByteArray(), Base64.DEFAULT).replace("\n","")
    }

    fun decodificarBase64( txt: String):String{
        val textoDecodificado = Base64.decode(txt, Base64.DEFAULT).toString()
        return textoDecodificado

    }
