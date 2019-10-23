package com.danilovalerio.mycoin.data.model

data class Movimentacao(
    val id: String? = "", val valor: Double, val tipo: String, val data: String,
    val categoria: String? = "", val descricao: String? = ""
)