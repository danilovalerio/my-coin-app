package com.danilovalerio.mycoin.data

import androidx.lifecycle.MutableLiveData

class Database {

    private val mDatabase: MutableLiveData<MutableList<Movimentacao>> =
        MutableLiveData()

    fun insertMovimentacao(movimentacao: Movimentacao){
        var tmp = mDatabase.value

        if (tmp == null){
            tmp = mutableListOf()
            tmp.add(movimentacao)
        } else {
            tmp.add(movimentacao)
        }
        mDatabase.postValue(tmp)
    }

    fun getMovimentacoes() = mDatabase


}