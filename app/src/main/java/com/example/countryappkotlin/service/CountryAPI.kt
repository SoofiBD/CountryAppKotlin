package com.example.countryappkotlin.service

import com.example.countryappkotlin.model.Model
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CountryAPI {
    //https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json
    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries() : Single<List<Model>>
}