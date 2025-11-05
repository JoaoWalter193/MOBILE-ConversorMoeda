package com.example.conversordemoedas

import retrofit2.http.GET

interface ConversorAPI {

    @GET("BTC-USD")
    suspend fun getBitcoinDolar(): CurrencyResponse
    @GET("BTC-BRL")
    suspend fun getBitcoinReal(): CurrencyResponse

    @GET("USD-BRL")
    suspend fun getDolarReal(): CurrencyResponse
}