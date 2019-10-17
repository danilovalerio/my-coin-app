package com.danilovalerio.mycoin.activity

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.helper.mesesPortugues
import com.danilovalerio.mycoin.helper.msgShort
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_principal.*
import java.util.*

class PrincipalActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        auth = FirebaseAuth.getInstance()
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

}
