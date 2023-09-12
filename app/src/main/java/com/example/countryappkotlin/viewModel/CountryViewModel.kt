package com.example.countryappkotlin.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryappkotlin.model.Model
import com.example.countryappkotlin.service.CountryAPIservice
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver

class CountryViewModel : ViewModel() {

    private val countryService = CountryAPIservice()
    private val disposable = CompositeDisposable()

    val countries = MutableLiveData<List<Model>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refreshData() {
        getDataFromAPI()
    }

    private fun getDataFromAPI() {
        //countryLoadError.value = false
        loading.value = true

        /*disposable.add(
            countryService.getData()
                .subscribe({ countryList ->
                    countries.value = countryList
                    countryLoadError.value = false
                    loading.value = false
                }, { error ->
                    countryLoadError.value = true
                    loading.value = false
                    error.printStackTrace()
                })
        )*/

        disposable.add(
            countryService.getData()
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Model>>() {
                    override fun onSuccess(t: List<Model>) {
                        countries.value = t
                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        countryLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                }
            )
        )
    }
}