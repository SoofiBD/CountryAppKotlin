package com.example.countryappkotlin.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryappkotlin.model.Model
import com.example.countryappkotlin.service.countryDatabase
import kotlinx.coroutines.launch
import java.util.UUID

class FeedViewModel(application: Application) : BaseViewModel(application) {

    val countryLiveData = MutableLiveData<Model>()

    fun getDataFromRoom(uuid: Int){
        launch {
            val dao = countryDatabase(getApplication()).countryDao()
            val country = dao.getCountry(uuid)
            countryLiveData.value = country
        }

    }
}