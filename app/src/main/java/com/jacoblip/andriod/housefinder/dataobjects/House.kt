package com.jacoblip.andriod.housefinder.dataobjects

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject


open class House(
    @SerializedName("title") var title:String = "",
    @SerializedName("price") var price:String = "",
    @SerializedName("address") var address:String = "",
    @SerializedName("image")  var image:String = "",
    @SerializedName("lat")  var lat:Double = 0.0,
    @SerializedName("lng")  var lng:Double = 0.0

):RealmObject() {
}