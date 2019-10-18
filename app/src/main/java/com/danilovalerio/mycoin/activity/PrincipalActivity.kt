package com.danilovalerio.mycoin.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilovalerio.mycoin.adapter.MovimentacaoAdapter
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.helper.codificarBase64
import com.danilovalerio.mycoin.helper.mesesPortugues
import com.danilovalerio.mycoin.helper.msgShort
import com.danilovalerio.mycoin.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_principal.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap

class PrincipalActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: DatabaseReference
    private lateinit var usuarioRef: DatabaseReference
    //trata um eventListener para que ao fechar o app não fique atualizando com o firebase
    private lateinit var valueEventListenerUsuario: ValueEventListener
    private lateinit var valueEventListenerMovimentacao: ValueEventListener

    //Adapter
    lateinit var movimentacaoAdapter: MovimentacaoAdapter
    //Referencia movimentação
    private lateinit var movimentacaoRef: DatabaseReference
    private var movimentacaoList: MutableList<Movimentacao> = mutableListOf()

    private var despesaTotal: Double = 0.0
    private var receitaTotal: Double = 0.0
    private var resumoUsuario: Double = 0.0
    lateinit var mesAnoSelecionado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        //Adapter das movimentações
        movimentacaoAdapter =
            MovimentacaoAdapter(this, movimentacaoList)
        recViewMovimentos.adapter = movimentacaoAdapter
        recViewMovimentos.layoutManager = LinearLayoutManager(this)
        //recViewMovimentos.smoothScrollToPosition(movimentacaoList.size)

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().getReference()

        setSupportActionBar(toolbar)

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

//        menu.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        listeners()

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
                            "msg :" + listDados["valor"].toString()
                                    + listDados["tipo"]
                                    + listDados["data"]
                                    + listDados["categoria"]
                                    + listDados["descricao"]
                        )
                        valor = listDados["valor"].toString().toDouble()
                        tipo = listDados["tipo"].toString()
                        data = listDados["data"].toString()
                        categoria = listDados["categoria"].toString()
                        descricao = listDados["descricao"].toString()

                        movimentacaoList.add(Movimentacao(valor, tipo, data, categoria, descricao))
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
            startActivity(Intent(this, ReceitasActivity::class.java))
        }

        var mesSelecionado = ""

        calendarView.setOnMonthChangedListener({ widget, date ->
            msgShort(this, date.month.plus(1).toString())
            if (date.month < 10) {
                mesSelecionado = "0" + date.month.plus(1).toString()
            } else {
                mesSelecionado = date.month.plus(1).toString()
            }
            mesAnoSelecionado = mesSelecionado + date.year
        })
    }

    override fun onStart() {
        super.onStart()
        recuperarResumo()
        recuperarMovimentacoes()
    }

    override fun onStop() { //quando o app não é mais utilizado
        super.onStop()
        usuarioRef.removeEventListener(valueEventListenerUsuario)
        Log.i("eventoListener", "eventoListener foi removido")
//        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao) //está dando erro ao lançar movimentação

    }

}
