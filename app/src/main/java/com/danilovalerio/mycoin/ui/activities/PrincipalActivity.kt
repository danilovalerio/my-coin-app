package com.danilovalerio.mycoin.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilovalerio.mycoin.ui.adapter.MovimentacaoAdapter
import com.danilovalerio.mycoin.util.codificarBase64
import com.danilovalerio.mycoin.util.mesesPortugues
import com.danilovalerio.mycoin.util.msgShort
import com.danilovalerio.mycoin.data.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_principal.*
import java.text.DecimalFormat
import java.util.*
import com.danilovalerio.mycoin.R
import kotlin.collections.HashMap
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.danilovalerio.mycoin.viewmodels.MovimentacoesViewModel
import kotlinx.android.synthetic.main.activity_receitas.view.*


class PrincipalActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: DatabaseReference
    private lateinit var usuarioRef: DatabaseReference
    //trata um eventListener para que ao fechar o app não fique atualizando com o firebase
    private var valueEventListenerUsuario: ValueEventListener? = null
    private var valueEventListenerMovimentacao: ValueEventListener? = null

    //Adapter
    lateinit var movimentacaoAdapter: MovimentacaoAdapter
    //Referencia movimentação
    private lateinit var movimentacaoRef: DatabaseReference
    private var movimentacaoList: MutableList<Movimentacao> = mutableListOf()
    private lateinit var movimentacao: Movimentacao

    private var despesaTotal: Double = 0.0
    private var receitaTotal: Double = 0.0
    private var resumoUsuario: Double = 0.0
    lateinit var mesAnoSelecionado: String

    private lateinit var movimentacoesViewModel: MovimentacoesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        setSupportActionBar(toolbar)

        //Adapter das movimentações
        movimentacaoAdapter =
            MovimentacaoAdapter(this, movimentacaoList)
        recViewMovimentos.adapter = movimentacaoAdapter
        recViewMovimentos.layoutManager = LinearLayoutManager(this)
        //recViewMovimentos.smoothScrollToPosition(movimentacaoList.size)

        //inicializar o ViewModel Movimentacao pode ser feito em fragments e am activities
        movimentacoesViewModel = ViewModelProviders.of(this).get(MovimentacoesViewModel::class.java)

        movimentacoesViewModel.getMovimentacoes()?.observe(this, androidx.lifecycle.Observer { data ->
            data?.let {
                if(it.isEmpty()){
                    msgShort(this,"Lista vazia!")
                } else {
                    movimentacaoAdapter.add(it)
                }

            }
        })

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().getReference()

        setSupportActionBar(toolbar)

        configurarCalendario()
        swipe() //evendo deslizar
//        menu.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        listeners()

    }

    private fun configurarCalendario() {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.WEDNESDAY)
            .setMinimumDate(CalendarDay.from(1930, 0, 1))
            .setMaximumDate(CalendarDay.from(2060, 11, 31))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        calendarView.setTitleMonths(mesesPortugues())

        var dataAtual: CalendarDay = calendarView.currentDate
        var mesAtual = dataAtual.month.plus(1).toString()
        if (mesAtual.length < 2) {
            mesAtual = "0" + mesAtual
        }
        mesAnoSelecionado = mesAtual + dataAtual.year.toString()
    }

    fun swipe() {

        val itemTouch = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder, target: ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    // move item in `fromPos` to `toPos` in adapter.
                    return true// true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    Log.i("swipe", "Item foi arrastado para START ou END.")
                    excluirMovimentacao(viewHolder)
                }

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder
                ): Int {
                    var dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                    //define as direções do swipe < START | END >
                    var swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                    return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
                    //return super.getMovementFlags(recyclerView, viewHolder)
                }
            })

        itemTouch.attachToRecyclerView(recViewMovimentos) //aplica o evento callback ao RecyclerView



    }

    private fun excluirMovimentacao(viewHolder: ViewHolder){//passa a posição do item viewHolder
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)

        //configuração do AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta")
        alertDialog.setMessage("Você tem certeza que deseja excluir esta movimentação?")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
            dialogInterface, id ->
            val pos = viewHolder.layoutPosition //pega posição do item no recyclerView
            movimentacao = movimentacaoList[pos]

            val emailUsuario = auth.currentUser?.email.toString()
            val idUsuario = codificarBase64(emailUsuario)

            movimentacaoRef = firebase.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado)
            movimentacaoRef.child(movimentacao.id!!).removeValue()

            atualizarSaldo(movimentacao.valor.toDouble(), movimentacao.tipo)

            movimentacaoAdapter.notifyItemRemoved(pos)
        })

        alertDialog.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialogInterface, id ->
            msgShort(this, "Exclusão cancelada.")
            movimentacaoAdapter.notifyDataSetChanged()
        })

        alertDialog.create()
        alertDialog.show()

    }

    private fun atualizarSaldo(valor: Double, tipo: String){

        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebase.child("usuarios").child(idUsuario)

        if(tipo.equals("r")){
            receitaTotal = receitaTotal - valor
            usuarioRef.child("receitaTotal").setValue(receitaTotal)
        } else {
            despesaTotal = despesaTotal - valor
            usuarioRef.child("despesaTotal").setValue(despesaTotal)
        }
    }

    private fun recuperarMovimentacoes() {
        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)

        movimentacaoRef = firebase.child("movimentacao")
            .child(idUsuario)
            .child(mesAnoSelecionado)

        valueEventListenerUsuario =
            movimentacaoRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    movimentacaoList.clear()
                    var id: String? = null
                    var valor: Double? = null
                    var tipo: String? = null
                    var data: String? = null
                    var categoria: String? = null
                    var descricao: String? = null

                    lateinit var listDados: MutableMap<Any, Any>

                    for (dados in dataSnapshot.children) {
                        listDados = dados.value as MutableMap<Any, Any>

                        Log.i(
                            "testeDados",
                            "msg :"+ dados.key.toString()
                                    + listDados["valor"].toString()
                                    + listDados["tipo"]
                                    + listDados["data"]
                                    + listDados["categoria"]
                                    + listDados["descricao"]
                        )
                        id = dados.key.toString()
                        valor = listDados["valor"].toString().toDouble()
                        tipo = listDados["tipo"].toString()
                        data = listDados["data"].toString()
                        categoria = listDados["categoria"].toString()
                        descricao = listDados["descricao"].toString()

                        movimentacaoList.add(
                            Movimentacao(
                                id,
                                valor,
                                tipo,
                                data,
                                categoria,
                                descricao
                            )
                        )
                    }

                    movimentacaoAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("erroBanco", databaseError.message)
                }
            })

//        Log.i("MES", "mes: "+mesAnoSelecionado)
    }

    private fun recuperarResumo() {
        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)
        usuarioRef = firebase.child("usuarios").child(idUsuario)

        Log.i("eventoListener", "eventoListener foi adicionado")
        valueEventListenerUsuario = usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    val anyMap: HashMap<Any, Any>
                    anyMap = dataSnapshot.getValue() as HashMap<Any, Any>

                    val valorDespesa = anyMap.getValue("despesaTotal").toString()
                    val valorReceita = anyMap.getValue("receitaTotal").toString()
                    despesaTotal = valorDespesa.toDouble()
                    receitaTotal = valorReceita.toDouble()

                    resumoUsuario = receitaTotal.minus(despesaTotal)

                    val decFormat: DecimalFormat = DecimalFormat("0.##")
                    val resultFormatado = decFormat.format(resumoUsuario)

                    tvSaudacao.setText("Olá " + anyMap.getValue("nome"))
                    tvSaldoGeral.setText("R$ " + resultFormatado)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("erroBanco", databaseError.message)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu) //converte o xml em view
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSair -> {
                val userAutent = auth.currentUser?.email
                auth.signOut()
                msgShort(
                    this,
                    "Usuário anterior: " + userAutent + "\nUsuário atual: " + auth.currentUser?.email
                )
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listeners() {
        menu_despesa.setOnClickListener() {
            msgShort(this, "Cadastro de despesa")
            startActivity(Intent(this, DespesasActivity::class.java))
        }

        menu_receita.setOnClickListener() {
            msgShort(this, "Cadastro de receita")
//            startActivity(Intent(this, ReceitasActivity::class.java))
            dialogoAddMovimentacaoReceita()
        }

        var mesSelecionado = ""

        calendarView.setOnMonthChangedListener({ widget, date ->
            msgShort(this, date.month.plus(1).toString())
            if (date.month < 9) {
                mesSelecionado = "0" + date.month.plus(1).toString()
            } else {
                mesSelecionado = date.month.plus(1).toString()
            }
            mesAnoSelecionado = mesSelecionado + date.year
            if (valueEventListenerMovimentacao != null) {
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacao!!)//remove o evento anterior
            }
            recuperarMovimentacoes()
        })
    }

    override fun onStart() {
        super.onStart()
        recuperarResumo()
//        recuperarMovimentacoes()
    }

    override fun onStop() { //quando o app não é mais utilizado
        super.onStop()
        if (valueEventListenerUsuario != null) {
            usuarioRef.removeEventListener(valueEventListenerUsuario!!)
        }
        Log.i("eventoListener", "eventoListener foi removido")
        if (valueEventListenerMovimentacao != null) {
            movimentacaoRef.removeEventListener(valueEventListenerMovimentacao!!) //está dando erro ao lançar movimentação
        }

    }

    private fun dialogoAddMovimentacaoReceita(){
        val layout = LayoutInflater.from(this)
            .inflate(R.layout.activity_dialog_receitas, null, false)

        val dialog = AlertDialog.Builder(this)
        dialog.setView(layout)
        dialog.setNegativeButton("Cancelar", null)
        dialog.setPositiveButton("Salvar"){
            d, i ->
            //salvar nota
            val movimentacao = Movimentacao(
                null,
                layout.etValor.text.toString().toDouble(),
                "r",
                layout.etData.text.toString(),
                layout.etCategoria.text.toString(),
                layout.etDescricao.text.toString()
            )

            movimentacoesViewModel.salvar(movimentacao)
        }

        dialog.create().show()
    }

}
