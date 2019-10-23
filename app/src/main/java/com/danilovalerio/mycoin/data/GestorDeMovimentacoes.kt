package com.danilovalerio.mycoin.data

import androidx.lifecycle.MutableLiveData
import com.danilovalerio.mycoin.data.model.Movimentacao


//camada de modelo que gerencia a obtencao das movimentacoes
class GestorDeMovimentacoes {

/**Mock exemplo
    private val data = mutableListOf<Movimentacao>(
        Movimentacao(
            "0",
            1000.0,
            "r",
            "23/10/2019",
            "salario",
            "salário mensal"
        ),
        Movimentacao(
            "1",
            1010.0,
            "r",
            "23/11/2019",
            "salario",
            "salário mensal"
        ),
        Movimentacao(
            "2",
            1020.0,
            "r",
            "23/12/2019",
            "salario",
            "salário mensal"
        ),
        Movimentacao(
            "3",
            1030.0,
            "r",
            "23/01/2020",
            "salario",
            "salário mensal"
        ),
        Movimentacao(
            "4",
            1040.0,
            "r",
            "23/02/2020",
            "salario",
            "salário mensal"
        )
    )
*/
    private val data: MutableLiveData<MutableList<Movimentacao>> = MutableLiveData()

    fun getMovimentacoes() = data

    fun addMovimentacao(mMovimentacao: Movimentacao) {
        var tmp = data.value
        if (tmp == null){
            tmp = mutableListOf()
            tmp.add(mMovimentacao)
        } else {
            tmp?.add(mMovimentacao)
        }
        data.postValue(tmp)
    }
}