package com.jacoblip.andriod.housefinder.UI.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.MapView
import com.google.android.material.snackbar.Snackbar
import com.jacoblip.andriod.housefinder.R
import com.jacoblip.andriod.housefinder.adapters.HouseResultsAdapter
import com.jacoblip.andriod.housefinder.dataobjects.House
import com.jacoblip.andriod.housefinder.util.InternetConectivity
import com.jacoblip.andriod.housefinder.util.Util
import com.jacoblip.andriod.housefinder.util.WifiReceiver
import com.jacoblip.andriod.housefinder.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_map.view.*

class RecyclerViewHousesFragment(context: Context):Fragment() {
    private lateinit var houseResultsRV: RecyclerView
    lateinit var viewModel: MainViewModel
    lateinit var wifiReceiver: WifiReceiver

    interface Callbacks {
        fun onHouseSelected(house: House)
    }

    private var callbacks: Callbacks? = null

    //the callback functions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_house_rv, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        wifiReceiver = WifiReceiver(requireContext())

        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        houseResultsRV = view.houseResultsRecyclerView
        houseResultsRV.layoutManager = LinearLayoutManager(context)
        houseResultsRV.adapter = HouseResultsAdapter(viewModel.localHouses,callbacks!!)
        setUpObservers(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpObservers(view: View){
        viewModel.houses?.observe(viewLifecycleOwner, Observer { houses ->
            if (houses != null&& viewModel.hasInternet.value==true) {
                houseResultsRV.adapter = HouseResultsAdapter(houses,callbacks!!)

            }
        })

        Util.hasInternet.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.hasInternet.postValue(true)
                viewModel.getHouses()
            }else{
                viewModel.hasInternet.postValue(false)
                viewModel.getHousesFromLocal()
            }
        })
    }


    companion object {

        fun newInstance(context: Context): RecyclerViewHousesFragment {
            return RecyclerViewHousesFragment(context)
        }
    }
}