package com.jacoblip.andriod.housefinder.repository


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.jacoblip.andriod.housefinder.dataobjects.House
import com.jacoblip.andriod.housefinder.googlemaps.GeoFinder
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import java.time.LocalTime

class Repository(context: Context) {
    var context = context


    @RequiresApi(Build.VERSION_CODES.O)
    fun scrapeForHouses(): Array<House> {
        var j = 0
        Log.i("houseFun","${LocalTime.now()} - repository fun start")
        var resultHouses: ArrayList<House> = arrayListOf()
        var doc =
            Jsoup.connect("https://www.anglo-saxon.co.il/en/branches/single/?id=3#our_properties")
                .get()
        var allinfo = doc.getElementsByClass("col-lg-6 col-md-6 col-sm-6 col-xs-12")
        // Log.i("data",allinfo.toString())
        for (i in allinfo) {
            var title = i.getElementsByClass("appart_box_title").html()
            var location = i.getElementsByClass("appart_box_location").html()
            var image = i.getElementsByClass("appart_boc_pic").attr("style")
            var price = i.getElementsByClass("appart_box_bottom clearfix").after("מחיר").html()
            var img = image.subSequence(25, image.length - 3).toString()
            if (img[0] == 'a') {
                img = "https://" + img
            }
            location = location.substring(52, location.lastIndex + 1)
            var index = price.indexOf('₪')
            var thePrice = ""
            var amount = 0
            while (amount < 2) {
                if (price[index] == ' ')
                    amount++
                thePrice = price[index] + thePrice
                index--
            }
            var IntPrice = thePrice.subSequence(1,thePrice.length-2).toString()
            var str = ""
            for(i in IntPrice){
               if(i!=',')
                   str += i
            }
            if(str.toInt()<100000){
                thePrice= "Rent: $thePrice"
            }

            var latLng = GeoFinder.getLocationFromAddress(context,location)
            var lat = 0.0
            var lng = 0.0
            if(latLng!=null){
                lat = latLng.latitude
                lng = latLng.longitude
            }

            val house = House(title, thePrice, location, img,lat,lng)
            if(house.image[0]=='h')
                resultHouses.add(house)

            Log.i("houseFun","${LocalTime.now()} -$j repository loop")
            j++
        }
        //(resultHouses.toTypedArray())
        Log.i("houseFun","${LocalTime.now()} - repository fun end")
        return resultHouses.toTypedArray()

    }

    fun getHousesFromLocal():Array<House>{

        val realm = Realm.getDefaultInstance()
        val houses = realm.where(House::class.java).findAll().toTypedArray()
        return houses
    }
}