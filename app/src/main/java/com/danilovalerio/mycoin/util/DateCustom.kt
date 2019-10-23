package com.danilovalerio.mycoin.util

import java.text.SimpleDateFormat

fun dataAtual(): String {
    val data = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    return sdf.format(data)
}

fun dataAtualHoraMinSeg(): String {
    val data = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
    return sdf.format(data)
}

//retorna mes e ano de data
fun mesAnoDataEscolhida(data: String): String{
    val data = data.split("/")
    return data[1]+data[2]
}
