package com.danilovalerio.mycoin.activity

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilovalerio.mycoin.MovimentacaoAdapter
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.helper.codificarBase64
import com.danilovalerio.mycoin.helper.mesesPortugues
import com.danilovalerio.mycoin.helper.msgShort
import com.danilovalerio.mycoin.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_principal.*
import java.text.DecimalFormat
import java.util.*

class PrincipalActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: DatabaseReference
    private lateinit var usuarioRef: DatabaseReference
    //trata um eventListener para que ao fechar o app não fique atualizando com o firebase
    private lateinit var valueEventListenerUsuario: ValueEventListener

    //Adapter
    lateinit var movimentacaoAdapter: MovimentacaoAdapter

    private var despesaTotal: Double = 0.0
    private var receitaTotal: Double = 0.0
    private var resumoUsuario: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        //Mock para teste do Recycler
        val movimentacaoList: MutableList<Movimentacao> = mutableListOf(
            Movimentacao(100.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(200.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(300.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(150.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(215.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(150.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(123.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(100.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(100.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(200.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(300.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(150.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(215.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(150.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(123.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(100.0,"d","10/10/2019","Almoço","Almoço caro"),
            Movimentacao(111.0,"d","10/10/2019","Almoço","Almoço caro")
        )

        //Adapter de exemplo
        movimentacaoAdapter = MovimentacaoAdapter(this, movimentacaoList)
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

//        menu.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        listeners()

    }

    private fun recuperarResumo(){
        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)
        usuarioRef = firebase.child("usuarios").child(idUsuario)

        Log.i("eventoListener", "eventoListener foi adicionado")
        valueEventListenerUsuario = usuarioRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    val anyMap: HashMap<Any, Any>
                    anyMap = dataSnapshot.getValue() as HashMap<Any,Any>

                    val valorDespesa = anyMap.getValue("despesaTotal").toString()
                    val valorReceita = anyMap.getValue("receitaTotal").toString()
                    despesaTotal = valorDespesa.toDouble()
                    receitaTotal = valorReceita.toDouble()

                    resumoUsuario = receitaTotal.minus(despesaTotal)

                    val decFormat : DecimalFormat = DecimalFormat("0.##")
                    val resultFormatado = decFormat.format(resumoUsuario)

                    tvSaudacao.setText("Olá "+anyMap.getValue("nome"))
                    tvSaldoGeral.setText("R$ "+resultFormatado)
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
        when(item.itemId){
            R.id.menuSair -> {
                val userAutent = auth.currentUser?.email
                auth.signOut()
                msgShort(this,"Usuário anterior: "+userAutent+"\nUsuário atual: "+auth.currentUser?.email)
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listeners(){
        menu_despesa.setOnClickListener(){
            msgShort(this, "Cadastro de despesa")
            startActivity(Intent(this, DespesasActivity::class.java))
        }

        menu_receita.setOnClickListener(){
            msgShort(this, "Cadastro de receita")
            startActivity(Intent(this, ReceitasActivity::class.java))
        }

        calendarView.setOnMonthChangedListener({widget, date ->
            msgShort(this,date.month.plus(1).toString())
        })
    }

    override fun onStart() {
        super.onStart()
        recuperarResumo()
    }

    override fun onStop() { //quando o app não é mais utilizado
        super.onStop()
        usuarioRef.removeEventListener(valueEventListenerUsuario)
        Log.i("eventoListener", "eventoListener foi removido")

    }

}
