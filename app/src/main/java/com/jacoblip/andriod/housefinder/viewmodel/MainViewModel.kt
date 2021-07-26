package com.jacoblip.andriod.housefinder.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.jacoblip.andriod.housefinder.dataobjects.House
import com.jacoblip.andriod.housefinder.repository.Repository
import com.jacoblip.andriod.housefinder.util.InternetConectivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.LocalTime


private const val TAG  = "MainViewModel"
@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(repository: Repository, context: Context):ViewModel() {
    var context = context
    var localHouses:Array<House> = arrayOf()
    val repository = repository
    var houses:MutableLiveData<Array<House>>? = MutableLiveData()
    var hasInternet:MutableLiveData<Boolean>  = MutableLiveData()
    init {
        getHousesFromLocal()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getHouses(){
        Log.i("houseFun","${LocalTime.now()} - fun start")
       doAsync {
           Log.i("houseFun", "${LocalTime.now()} - coroutine start")
           val myHouses = repository.scrapeForHouses()
           houses?.postValue(myHouses)
           // gotHousesFromWeb.postValue(true)
           Log.i("houseFun", "${LocalTime.now()} - coroutine end")
       }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHousesFromLocal(){
        Log.i("houseFun","${LocalTime.now()} - houses from local start")
        val housesFromLocal = repository.getHousesFromLocal()
        localHouses = housesFromLocal
        houses?.postValue(housesFromLocal)
        Log.i("houseFun","${LocalTime.now()} - houses from local end")
    }
}
