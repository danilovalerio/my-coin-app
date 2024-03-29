package com.danilovalerio.mycoin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.R.color.colorPrimaryDespesa
import com.danilovalerio.mycoin.R.color.colorPrimaryReceita
import com.danilovalerio.mycoin.data.Movimentacao
import kotlinx.android.synthetic.main.item_rv_movimentos.view.*
import kotlin.math.absoluteValue

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
        holder.bindView(movimentacaoList[position], context)
    }

    class MovimentacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo = itemView.tvTitulo
        val tvDescricao = itemView.tvDescricao
        val tvValor = itemView.tvValor

        fun bindView(movimentacao: Movimentacao, context: Context) {
            tvTitulo.text = movimentacao.categoria
            tvDescricao.text = movimentacao.descricao
            tvValor.text = movimentacao.valor.toString()

            if(movimentacao.tipo == "d"){
                tvValor.setTextColor(context.getString(colorPrimaryDespesa.absoluteValue).toColorInt())
                tvValor.setText("- "+movimentacao.valor)
            } else {
                tvValor.setTextColor(context.getString(colorPrimaryReceita.absoluteValue).toColorInt())
            }




        }
    }

    fun add(itens: List<Movimentacao>){
        movimentacaoList.clear()
        movimentacaoList.addAll(itens)
        notifyDataSetChanged()
    }
}