package com.example.countryappkotlin.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryappkotlin.model.Model
import com.example.countryappkotlin.service.CountryAPIservice
import com.example.countryappkotlin.service.countryDatabase
import com.example.countryappkotlin.util.CustomSharedPreferences
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : BaseViewModel(application) {

    private val countryService = CountryAPIservice()
    private val disposable = CompositeDisposable()
    private var customPreference = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Model>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refreshData() {

        val updateTime = customPreference.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPI()
        }
    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        loading.value = true
        launch {
            val countries = countryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(), "Countries from SQLite", Toast.LENGTH_LONG).show()
        }
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
                        storeinSQLite(t)
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

    private fun showCountries(countryList: List<Model>) {
        countries.value = countryList
        countryLoadError.value = false
        loading.value = false
    }

    private fun storeinSQLite(countryList: List<Model>) {
        launch {
            val dao =  countryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*countryList.toTypedArray())
            var i = 0
            while (i < listLong.size) {
                countryList[i].uuid = listLong[i].toInt()
                i++
            }
            showCountries(countryList)
        }

        customPreference.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}