package com.jacoblip.andriod.housefinder.googlemaps

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.jacoblip.andriod.housefinder.dataobjects.House

open class ClusterItem(
    mycontext: Context,
    house: House,
    latLng: LatLng
):ClusterItem {
    var context = mycontext
    var house = house
    var location = latLng


    override fun getSnippet(): String {
       return  house.address
    }

    override fun getTitle(): String {
        return house.title
    }

    override fun getPosition(): LatLng? {
        return location
    }


}