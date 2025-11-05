package com.example.conversordemoedas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var textViewReal: TextView
    private lateinit var textViewDolar: TextView
    private lateinit var textViewBtc: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textViewReal = findViewById(R.id.textViewReal)
        textViewDolar = findViewById(R.id.textViewDolar)
        textViewBtc = findViewById(R.id.textViewBitcoin)
        textViewReal.setText("R$ %.2f".format(Valores.valorReal))
        textViewDolar.setText("U\$D %.2f".format(Valores.valorDOlar))
        textViewBtc.setText("BTC %.4f".format(Valores.valorBtc))

    }

    fun irConverter(view: View){
        val intent = Intent(this, ConvertResources::class.java)
        startActivity(intent)
    }


}