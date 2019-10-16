package com.danilovalerio.mycoin.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object ConfiguracaoFirebase {

    private lateinit var autenticacao : FirebaseAuth
    private lateinit var firebase : DatabaseReference

    fun getFirebaseDataBase(): DatabaseReference{
        if(firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference()
        }
        return firebase
    }

    fun getFirebaseAutenticacao(): FirebaseAuth{
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance()
        }
        return autenticacao
    }
}