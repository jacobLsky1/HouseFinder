package com.jacoblip.andriod.housefinder.UI.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.jacoblip.andriod.housefinder.R
import com.jacoblip.andriod.housefinder.adapters.HouseResultsAdapter
import com.jacoblip.andriod.housefinder.dataobjects.House
import com.jacoblip.andriod.housefinder.googlemaps.ClusterItem
import com.jacoblip.andriod.housefinder.googlemaps.ClusterManagerRender
import com.jacoblip.andriod.housefinder.googlemaps.GeoFinder
import com.jacoblip.andriod.housefinder.util.InternetConectivity
import com.jacoblip.andriod.housefinder.util.Util
import com.jacoblip.andriod.housefinder.viewmodel.MainViewModel
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.jetbrains.anko.doAsync


class MapAndRVFragment(context: Context):Fragment(){

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_and_rv_container, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers(view)
        setUpFragments()
    }

    fun setUpObservers(view: View){
        val snackBar: Snackbar =
            Snackbar.make(view, "Can't Connect To Web..", Snackbar.LENGTH_INDEFINITE)
                .setAction("GO TO SETTINGS") {
                    context?.let { it1 -> InternetConectivity.connectToInternet(context!!) }
                }
        Util.hasInternet.observe(viewLifecycleOwner, Observer { it ->
            if (!it) {
                snackBar.show()
            } else {
                snackBar.dismiss()
            }
        })
    }
    fun  setUpFragments(){

        doAsync {
            val mapFragment = MapFragment.newInstance(requireContext())
            childFragmentManager
                .beginTransaction()
                .add(R.id.containerForMapFragment, mapFragment)
                .commit()
        }



            val rvFragment = RecyclerViewHousesFragment.newInstance(requireContext())
            childFragmentManager
                .beginTransaction()
                .add(R.id.containerForRVFragment, rvFragment)
                .commit()
    }

        companion object {
            fun newInstance(context: Context): MapAndRVFragment {
                return MapAndRVFragment(context)
            }
        }


    }
