package com.example.conversordemoedas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConvertResources : AppCompatActivity() {

    private lateinit var spinnerMoedaEntrada: Spinner
    private lateinit var spinnerMoedaSaida: Spinner
    private lateinit var valorConverter: EditText
    private lateinit var progressBarRequest: ProgressBar
    private lateinit var valorMostrar: EditText
    private lateinit var conversorAPI: ConversorAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_convert_resources)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerEntrada = findViewById<Spinner>(R.id.spinnerMoedaEntrada)
        val spinnerSaida = findViewById<Spinner>(R.id.spinnerMoedaSaida)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.ChicoMoedas,
            R.layout.spinner_item
        )

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        spinnerEntrada.adapter = adapter
        spinnerSaida.adapter = adapter


        progressBarRequest = findViewById(R.id.progressBarRequest)
        valorMostrar = findViewById(R.id.editTextNumber2Mostrar)

        // bagulho da API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br/last/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        conversorAPI = retrofit.create(ConversorAPI::class.java)
    }

    fun converter(view: View) {
        spinnerMoedaEntrada = findViewById(R.id.spinnerMoedaEntrada)
        spinnerMoedaSaida = findViewById(R.id.spinnerMoedaSaida)
        valorConverter = findViewById(R.id.editTextNumberValorConverter)


        // CORREÇÃO CRÍTICA: usar spinnerMoedaSaida aqui ↓
        val selecionadoEntrada = spinnerMoedaEntrada.selectedItem.toString()
        val selecionadoSaida = spinnerMoedaSaida.selectedItem.toString() // CORREÇÃO

        if (selecionadoEntrada != selecionadoSaida) {
            val valorParaConverter = valorConverter.text.toString().toDouble()
            var saldoSuficiente = false
            when (selecionadoEntrada) {
                "Real R$" -> {
                    if (valorParaConverter <= Valores.valorReal) saldoSuficiente = true
                }
                "Dólar U\$D" -> {
                    if (valorParaConverter <= Valores.valorDOlar) saldoSuficiente = true
                }
                "Bitcoin BTC" -> {
                    if (valorParaConverter <= Valores.valorBtc) saldoSuficiente = true
                }
            }
            if (saldoSuficiente) {
                if (selecionadoEntrada == "Real R$" && selecionadoSaida == "Dólar U\$D") {
                    lifecycleScope.launch {
                        try {
                            showProgressBar()
                            val response = withContext(Dispatchers.IO) {
                                conversorAPI.getDolarReal()
                            }
                            hideProgressBar()

                            var cota = response.values.first().high.toDouble()
                            var valorConvertido = valorParaConverter / cota

                            Valores.valorReal = Valores.valorReal - valorParaConverter
                            Valores.valorDOlar = Valores.valorDOlar + valorConvertido

                            valorMostrar.setText("%.2f".format(valorConvertido))
                            valorConverter.setText("")
                        } catch (e: Exception) {
                            Log.e("ConvertResource", "Erro ao buscar conversão", e)
                        }
                    }
                }
                if (selecionadoEntrada == "Real R$" && selecionadoSaida == "Bitcoin BTC") {
                    lifecycleScope.launch {
                        try {
                            showProgressBar()
                            val response = withContext(Dispatchers.IO) {
                                conversorAPI.getBitcoinReal()
                            }
                            hideProgressBar()

                            var cota = response.values.first().high.toDouble()
                            var valorConvertido = valorParaConverter / cota

                            Valores.valorReal = Valores.valorReal - valorParaConverter
                            Valores.valorBtc = Valores.valorBtc + valorConvertido

                            valorMostrar.setText("%.4f".format(valorConvertido))
                            valorConverter.setText("")
                        } catch (e: Exception) {
                            Log.e("ConvertResource", "Erro ao buscar conversão", e)
                        }
                    } // FALTANDO ESTE COLCHETE
                }
                if (selecionadoEntrada == "Dólar U\$D" && selecionadoSaida == "Real R$") {
                    lifecycleScope.launch {
                        try {
                            showProgressBar()
                            val response = withContext(Dispatchers.IO) {
                                conversorAPI.getDolarReal()
                            }

                            hideProgressBar()

                            var cota = response.values.first().high.toDouble()

                            var valorConvertido = valorParaConverter * cota

                            Valores.valorDOlar = Valores.valorDOlar - valorParaConverter
                            Valores.valorReal = Valores.valorReal + valorConvertido



                            valorMostrar.setText("%.2f".format(valorConvertido))
                            valorConverter.setText("")

                        } catch (e: Exception) {
                            Log.e("ConvertResource", "Erro ao buscar conversão", e)
                            println("ERRO")
                        }
                    } // FALTANDO ESTE COLCHETE
                }
                if (selecionadoEntrada == "Dólar U\$D" && selecionadoSaida == "Bitcoin BTC") {
                    lifecycleScope.launch {
                        try {
                            showProgressBar()
                            val response = withContext(Dispatchers.IO) {
                                conversorAPI.getBitcoinDolar()
                            }
                            hideProgressBar()

                            var cota = response.values.first().high.toDouble()
                            var valorConvertido = valorParaConverter / cota

                            Valores.valorDOlar = Valores.valorDOlar - valorParaConverter
                            Valores.valorBtc = Valores.valorBtc + valorConvertido

                            valorMostrar.setText("%.4f".format(valorConvertido))
                            valorConverter.setText("")
                        } catch (e: Exception) {
                            Log.e("ConvertResource", "Erro ao buscar conversão", e)
                        }
                    } // FALTANDO ESTE COLCHETE
                }
                if (selecionadoEntrada == "Bitcoin BTC" && selecionadoSaida == "Real R$") {
                    lifecycleScope.launch {
                        try {
                            showProgressBar()
                            val response = withContext(Dispatchers.IO) {
                                conversorAPI.getBitcoinReal()
                            }
                            hideProgressBar()

                            var cota = response.values.first().high.toDouble()
                            var valorConvertido = valorParaConverter * cota

                            Valores.valorBtc = Valores.valorBtc - valorParaConverter
                            Valores.valorReal = Valores.valorReal + valorConvertido

                            valorMostrar.setText("%.2f".format(valorConvertido))
                            valorConverter.setText("")
                        } catch (e: Exception) {
                            Log.e("ConvertResource", "Erro ao buscar conversão", e)
                        }
                    }
                }
                if (selecionadoEntrada == "Bitcoin BTC" && selecionadoSaida == "Dólar U\$D") {
                    lifecycleScope.launch {
                        try {
                            showProgressBar()
                            val response = withContext(Dispatchers.IO) {
                                conversorAPI.getBitcoinDolar()
                            }
                            hideProgressBar()

                            var cota = response.values.first().high.toDouble()
                            var valorConvertido = valorParaConverter * cota

                            Valores.valorBtc = Valores.valorBtc - valorParaConverter
                            Valores.valorDOlar = Valores.valorDOlar + valorConvertido

                            valorMostrar.setText("%.2f".format(valorConvertido))
                            valorConverter.setText("")
                        } catch (e: Exception) {
                            Log.e("ConvertResource", "Erro ao buscar conversão", e)
                        }
                    }
                }
            } else {
                // ESTE else ESTÁ NO LUGAR ERRADO - deveria ser para saldo insuficiente
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor selecione duas moedas diferentes", Toast.LENGTH_SHORT).show()
        }
    }

    fun showProgressBar() {
        progressBarRequest.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarRequest.visibility = View.GONE
    }

    fun voltar(view : View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}