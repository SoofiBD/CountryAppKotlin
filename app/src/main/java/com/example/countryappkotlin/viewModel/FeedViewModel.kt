package com.example.countryappkotlin.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryappkotlin.model.Model

class FeedViewModel : ViewModel() {

    val countryLiveData = MutableLiveData<List<Model>>()

    fun getDataFromRoom(){

        val country1 = Model("India", "New Delhi", "Asia", "Rupee", "Hindi", "https://restcountries.eu/data/ind.svg")
        countryLiveData.value = arrayListOf<Model>(country1)
    }
}