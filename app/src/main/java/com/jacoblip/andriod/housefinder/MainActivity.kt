package com.jacoblip.andriod.housefinder

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jacoblip.andriod.housefinder.UI.fragments.HouseDetailFragment
import com.jacoblip.andriod.housefinder.UI.fragments.MapAndRVFragment
import com.jacoblip.andriod.housefinder.UI.fragments.RecyclerViewHousesFragment
import com.jacoblip.andriod.housefinder.dataobjects.House
import com.jacoblip.andriod.housefinder.repository.Repository
import com.jacoblip.andriod.housefinder.util.WifiReceiver
import com.jacoblip.andriod.housefinder.viewmodel.MainViewModel
import com.jacoblip.andriod.housefinder.viewmodel.viewModelProviderFactory
import io.realm.Realm
import io.realm.RealmConfiguration


class MainActivity : AppCompatActivity(), RecyclerViewHousesFragment.Callbacks {

    lateinit var viewModel: MainViewModel
    private lateinit var wifiReceiver:WifiReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        var realmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(realmConfiguration)
        val repository = Repository(applicationContext)
        val viewModelProviderFactory = viewModelProviderFactory(repository,applicationContext)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        wifiReceiver = WifiReceiver(applicationContext)


        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }




        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            val fragment = MapAndRVFragment.newInstance(applicationContext)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(wifiReceiver, filter)
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiReceiver)
    }

    override fun onHouseSelected(house: House) {
        val fragment = HouseDetailFragment.newInstance(applicationContext,house)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}