package com.danilovalerio.mycoin.model

import com.google.firebase.database.Exclude

data class Usuario(
    @Exclude @get:Exclude val idUsuario: String,
    val nome: String,
    val email: String,
    @Exclude @get:Exclude val senha: String
)