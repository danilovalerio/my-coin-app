package com.danilovalerio.mycoin.util

fun mask(txt: String): String {
    var saida = ""
    var texto = txt
    var tam = txt.length

    when (tam) {
        1 -> saida = "0,0" + txt
        2 -> saida = "0," + txt
        3 -> saida = txt.substring(0, 1) + "," + txt.substring(txt.length - 2, txt.length)
    }

    if (tam > 3) {
        saida = "," + txt.substring(txt.length - 2, txt.length)
        texto = texto.substring(0, txt.length - 2)
        //print(texto)
        saida = texto.substring(0, texto.length) + saida

    }


    return "R$ " + saida
}
