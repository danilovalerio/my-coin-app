package com.danilovalerio.mycoin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.model.Movimentacao
import kotlinx.android.synthetic.main.item_rv_movimentos.view.*

class MovimentacaoAdapter(
    private val context: Context,
    private var movimentacaoList: MutableList<Movimentacao>
) :
    RecyclerView.Adapter<MovimentacaoAdapter.MovimentacaoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovimentacaoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_movimentos, parent, false)
        return MovimentacaoViewHolder(view)
    }

    override fun getItemCount(): Int = movimentacaoList.size

    override fun onBindViewHolder(
        holder: MovimentacaoViewHolder,
        position: Int
    ) {
        holder.bindView(movimentacaoList[position])
    }

    class MovimentacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo = itemView.tvTitulo
        val tvDescricao = itemView.tvDescricao
        val tvValor = itemView.tvValor

        fun bindView(movimentacao: Movimentacao) {
            tvTitulo.text = movimentacao.categoria
            tvDescricao.text = movimentacao.descricao
            tvValor.text = movimentacao.valor.toString()
        }
    }
}