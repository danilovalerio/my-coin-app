package com.danilovalerio.mycoin.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.danilovalerio.mycoin.R
import com.danilovalerio.mycoin.helper.*
import com.danilovalerio.mycoin.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_despesas.*


class DespesasActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: DatabaseReference
    private lateinit var movimentacao: Movimentacao
    private var despesaTotal: Double = 0.0
    private var despesaGerada: Double = 0.0
    private var despesaAtualizada: Double = 0.0 //total + atualizada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despesas)

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().getReference()
        etData.setText(dataAtual())

        etValor.requestFocus()

        recuperarDespesaTotal()

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
                    movimentacao = Movimentacao("",
                        valor.toDouble(),
                        "d",
                        mesAnoDataEscolhida(etToString(etData)),
                        if (!etToString(etCategoria).isNullOrEmpty()) etToString(etCategoria) else null,
                        if (!etToString(etDescricao).isNullOrEmpty()) etToString(etDescricao) else null
                    )

                    salvarMovimentacao(movimentacao)

                    despesaGerada = valor.toDouble()
                    despesaAtualizada = (despesaTotal.plus(despesaGerada))

                    atualizarDespesaTotal(despesaAtualizada)

                }
            } catch (e: Exception) {
                msgShort(this, "Erro ao salvar " + e)
            }
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

    private fun recuperarDespesaTotal(){
        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebase.child("usuarios").child(idUsuario)

        usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    val anyMap: HashMap<Any, Any>
                    anyMap = dataSnapshot.getValue() as HashMap<Any,Any>

                    val valorDespesa = anyMap.getValue("despesaTotal").toString()
                    despesaTotal = valorDespesa.toDouble()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG", databaseError.message)
            }
        })
    }

    private fun atualizarDespesaTotal(despesa: Double){

        val emailUsuario = auth.currentUser?.email.toString()
        val idUsuario = codificarBase64(emailUsuario)
        val usuarioRef: DatabaseReference = firebase.child("usuarios").child(idUsuario)

        usuarioRef.child("despesaTotal").setValue(despesa)

    }

}
