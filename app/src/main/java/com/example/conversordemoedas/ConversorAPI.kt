package com.example.conversordemoedas

import retrofit2.http.GET

interface ConversorAPI {

    @GET("https://economia.awesomeapi.com.br/last/BTC-USD")
    suspend fun getBitcoinDolar(): Response

    @GET("https://economia.awesomeapi.com.br/last/BTC-BRL")
    suspend fun getBitcoinReal(): Response

    @GET("https://economia.awesomeapi.com.br/last/USD-BRL")
    suspend fun getDolarReal(): Response
}