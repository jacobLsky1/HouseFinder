package com.jacoblip.andriod.housefinder.UI.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.jacoblip.andriod.housefinder.R
import com.jacoblip.andriod.housefinder.adapters.HouseResultsAdapter
import com.jacoblip.andriod.housefinder.dataobjects.House
import com.jacoblip.andriod.housefinder.googlemaps.ClusterItem
import com.jacoblip.andriod.housefinder.googlemaps.ClusterManagerRender
import com.jacoblip.andriod.housefinder.googlemaps.GeoFinder
import com.jacoblip.andriod.housefinder.viewmodel.MainViewModel
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.jetbrains.anko.doAsync
import java.time.LocalTime

class MapFragment(context: Context):Fragment(), OnMapReadyCallback {

    private var mapView: MapView? = null
    lateinit var viewModel: MainViewModel
    lateinit var progressBar: ProgressBar
    lateinit var clusterManager: ClusterManager<ClusterItem>
    lateinit var clusterManagerRender: ClusterManagerRender
    var clusterMarkers:ArrayList<ClusterItem> = arrayListOf()



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_house_map, container, false)
        progressBar = view.progressBar
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.houseResultsMapView) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.onResume()
        mapView!!.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpObserver(map: GoogleMap?){
        viewModel.houses?.observe(viewLifecycleOwner , Observer { houses->
            if(viewModel.houses!!.value?.isNotEmpty()!!&&(viewModel.hasInternet.value!=null&&viewModel.hasInternet.value!!)) {
                map!!.clear()
                Log.i("houseFun","${LocalTime.now()} - map fun start")
                setUpMarkers(houses,map)
                Log.i("houseFun","${LocalTime.now()} - map fun end")
            }
        })
    }

    private fun moveToCurrentLocation(markers: ArrayList<ClusterItem>): CameraUpdate {
        val builder = LatLngBounds.Builder()
        for (marker in markers) {
            builder.include(marker.position)
        }
        val bounds = builder.build()

        val padding = 200 // offset from edges of the map in pixels

        val cu: CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        return cu
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpMarkers(houses: Array<House>, map: GoogleMap) {
        Log.i("houseFun","${LocalTime.now()} - marker fun start")
        clusterManager = ClusterManager(context, map)
        clusterManagerRender = ClusterManagerRender(context!!, map, clusterManager)
        clusterManager.renderer = clusterManagerRender
            viewModel.houses?.observe(viewLifecycleOwner, Observer { gotHouses ->
                for (house in houses) {
                    if (house.lat != 0.0 && house.lng != 0.0) {
                        var latLng = LatLng(house.lat, house.lng)
                        var clusterItem = ClusterItem(context!!, house, latLng!!)
                        clusterManager.addItem(clusterItem)
                        clusterMarkers.add(clusterItem)
                    }
                }
                var cu = moveToCurrentLocation(clusterMarkers)
                clusterManager.cluster()
                map.clear()
                map!!.animateCamera(cu)
                progressBar.visibility = View.GONE
                saveToLocal(houses)
                Log.i("houseFun","${LocalTime.now()} - marker fun end")
            })
    }

    private fun saveToLocal(houses:Array<House>){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.deleteAll()
        realm.insert(houses.toMutableList())
        realm.commitTransaction()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(map: GoogleMap?) {
            setUpObserver(map)
    }


    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
    }


    companion object {
        fun newInstance(context: Context): MapFragment {
            return MapFragment(context)
        }
    }
}