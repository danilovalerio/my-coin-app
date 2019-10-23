package com.danilovalerio.mycoin.data.model

import com.google.firebase.database.Exclude

data class Usuario(
    @Exclude @get:Exclude val idUsuario: String, //exclude para o obj não ser enviado junto
    val nome: String,
    val email: String,
    @Exclude @get:Exclude val senha: String? = null,
    val receitaTotal: Double? = 0.00,
    val despesaTotal: Double? = 0.00

)