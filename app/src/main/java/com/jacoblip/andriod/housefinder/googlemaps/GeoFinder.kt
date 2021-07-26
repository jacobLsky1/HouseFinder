package com.jacoblip.andriod.housefinder.googlemaps

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.io.IOException


class GeoFinder {
    companion object{
        fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
            val coder = Geocoder(context)
            val address: List<Address>?
            var p1: LatLng? = null
            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null) {
                    return null
                }
                val location: Address = address[0]
                p1 = LatLng(location.getLatitude(), location.getLongitude())
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return p1
        }

    }
}