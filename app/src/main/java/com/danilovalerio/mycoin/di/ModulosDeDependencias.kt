package com.danilovalerio.mycoin.di

import com.danilovalerio.mycoin.data.Database
import com.danilovalerio.mycoin.data.GestorDeMovimentacoes
import com.danilovalerio.mycoin.viewmodels.MovimentacoesViewModel

import org.koin.android.viewmodel.ext.koin.viewModel

import org.koin.dsl.module.module

object ModulosDeDependencias {

    val moduloApp = module {
        single { Database() }

        factory { GestorDeMovimentacoes(get()) } //get procura uma instancia do database

        viewModel{ MovimentacoesViewModel(get())} //procura instancia do gestor de notas e passa no get
    }

    val moduloX = module {

    }
}