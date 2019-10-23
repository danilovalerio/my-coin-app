package com.danilovalerio.mycoin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danilovalerio.mycoin.data.GestorDeMovimentacoes
import com.danilovalerio.mycoin.data.Movimentacao

//classe que irá gerir modelo movimentacao
class MovimentacoesViewModel(val gestorDeMovimentacoes: GestorDeMovimentacoes) : ViewModel() {

    private var mMovimentacoes: MutableLiveData<MutableList<Movimentacao>>? = null

    fun getMovimentacoes(): LiveData<MutableList<Movimentacao>> {
        if(mMovimentacoes == null){
            mMovimentacoes = gestorDeMovimentacoes.getMovimentacoes()
        }

        return mMovimentacoes!!
    }

    fun salvar(mMovimentacao: Movimentacao){
        gestorDeMovimentacoes.addMovimentacao(mMovimentacao)
    }
}