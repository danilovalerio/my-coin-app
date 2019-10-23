package com.danilovalerio.mycoin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danilovalerio.mycoin.data.Database
import com.danilovalerio.mycoin.data.GestorDeMovimentacoes
import com.danilovalerio.mycoin.data.Movimentacao

//classe que irá gerir modelo movimentacao
class MovimentacoesViewModel: ViewModel() {
    private val database = Database()
    private val gestorDeMovimentacoes = GestorDeMovimentacoes(database)
    //mutableLiveData mantem os dados até a activity ser finalizada
    private var mMovimentacoes: MutableLiveData<MutableList<Movimentacao>>? = null

    fun getMovimentacoes() : LiveData<MutableList<Movimentacao>> {
        if(mMovimentacoes == null){
            mMovimentacoes = gestorDeMovimentacoes.getMovimentacoes()
        }

        return mMovimentacoes!!
    }

    fun salvar(mMovimentacao: Movimentacao){
        gestorDeMovimentacoes.addMovimentacao(mMovimentacao)
    }
}