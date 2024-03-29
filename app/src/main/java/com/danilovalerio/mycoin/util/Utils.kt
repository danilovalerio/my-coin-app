package com.danilovalerio.mycoin.util

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.toColorInt

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

fun mesesPortugues() : Array<String> {
    //definir valores predefinidos para meses
    val meses = arrayOf<String>("Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro")
    return meses
//    var semanas = arrayOf<String>("Seg","Ter","Qua","Qui","Sex","Sáb","Dom")
//    calendarView.setWeekDayLabels(semanas)
}

fun mudarEditTextHintColor(campo:EditText, cor: String){
    return campo.setHintTextColor(cor.toColorInt())
}