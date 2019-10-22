package com.danilovalerio.mycoin.activities

import android.os.Bundle
import android.util.Log
import com.danilovalerio.mycoin.R
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.helper.*
import com.danilovalerio.mycoin.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_despesas.*
import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.widget.doOnTextChanged


class ReceitasActivity : AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: DatabaseReference
    private lateinit var movimentacao: Movimentacao
    private var receitaTotal: Double = 0.0
    private var receitaGerada: Double = 0.0
    private var receitaAtualizada: Double = 0.0 //total + atualizada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receitas)

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().getReference()
        etData.setText(dataAtual())

        etValor.requestFocus()

        recuperarReceitaTotal()

        listeners()

    }

    private fun listeners() {
        fabSalvar.setOnClickListener() {
            val valor: String = etToString(etValor)

            try {
                if (etToString(etValor).isNullOrEmpty() || etToString(etData).isNullOrEmpty()) {
                    if (etToString(etValor).isNullOrEmpty()) {
                        etValor.requestFocus()
                        etValor.setError("Valor obrigatório")
                    }
                    if (etToString(etData).isNullOrEmpty()) {
                        etData.setError("Valor obrigatório")
                    }
                } else {
                    movimentacao = Movimentacao(null,
                        valor.toDouble(),
                        "r",
                        mesAnoDataEscolhida(etToString(etData)),
                        if (!etToString(etCategoria).isNullOrEmpty()) etToString(etCategoria) else null,
                        if (!etToString(etDescricao).isNullOrEmpty()) etToString(etDescricao) else null
                    )

                    salvarMovimentacao(movimentacao)

                    receitaGerada = valor.toDouble()
                    receitaAtualizada = (receitaTotal.plus(receitaGerada))

                    atualizarReceitaTotal(receitaAtualizada)

                }
            } catch (e: Exception) {
                msgShort(this, "Erro ao salvar " + e)
            }
        }

        //escuta a alteração no texto
        etValor.doOnTextChanged { text, start, count, after ->
            Log.i("texto", text.toString())
        }
    }

    fun salvarMovimentacao(movimentacao: Movimentacao) {
        val idUsuario = codificarBase64(auth.currentUser?.email.toString())

        try {

            firebase.child("movimentacao")
                .child(idUsuario) //e-mail será na base 64
                .child(movimentacao.data)
                .push() //para criar o idFirebase
                .setValue(movimentacao)

            msgShort(this, "Sucesso na inserção.")
            finish()

        } catch (e: Exception) {
            msgShort(this, "Falha na inserção:" + e.toString())
        }
    }

    private fun recuperarReceitaTotal(){
        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebase.child("usuarios").child(idUsuario)

        usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    val anyMap: HashMap<Any, Any>
                    anyMap = dataSnapshot.getValue() as HashMap<Any,Any>

                    val valorDespesa = anyMap.getValue("receitaTotal").toString()
                    receitaTotal = valorDespesa.toDouble()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG", databaseError.message)
            }
        })
    }

    private fun atualizarReceitaTotal(receita: Double){

        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebase.child("usuarios").child(idUsuario)

        usuarioRef.child("receitaTotal").setValue(receita)

    }

}
