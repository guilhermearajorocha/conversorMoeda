package com.example.coinconverter.ui.home

import retrofit2.Call
import retrofit2.http.GET

interface valoresService {
    @GET("/latest")
    fun fetchValores() : Call<Valores>;
}