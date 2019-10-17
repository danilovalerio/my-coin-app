package com.danilovalerio.mycoin.model

data class Movimentacao (val valor:Double,val tipo: String, val data:String,
                         val categoria:String? = "", val descricao:String? = "")