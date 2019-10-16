package com.danilovalerio.mycoin.helper

import android.content.Context
import android.widget.EditText
import android.widget.Toast

fun etToString(et: EditText): String {
    return et.text.toString()
}

fun msgShort(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun validarEmail(email: String): Boolean {
    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return true
    }
    return false
}

fun validarStr(s: String): Boolean{
    if(s.isNullOrEmpty()){
        return false
    }
    return true
}