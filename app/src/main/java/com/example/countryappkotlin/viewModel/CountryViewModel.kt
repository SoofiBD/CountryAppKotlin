package com.example.countryappkotlin.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryappkotlin.model.Model

class CountryViewModel : ViewModel() {
    val countries = MutableLiveData<List<Model>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refreshData() {
        val country1 = Model("India", "New Delhi", "Asia", "Rupee", "Hindi", "https://restcountries.eu/data/ind.svg")
        val country2 = Model("Australia", "Canberra", "Oceania", "Australian dollar", "English", "https://restcountries.eu/data/aus.svg")
        val country3 = Model("United States of America", "Washington, D.C.", "Americas", "United States dollar", "English", "https://restcountries.eu/data/usa.svg")

        val countryList = arrayListOf<Model>(country1, country2, country3)

        countries.value = countryList
        countryLoadError.value = false
        loading.value = false
    }
}