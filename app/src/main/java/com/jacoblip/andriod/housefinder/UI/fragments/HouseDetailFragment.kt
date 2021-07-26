package com.jacoblip.andriod.housefinder.UI.fragments

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jacoblip.andriod.housefinder.R
import com.jacoblip.andriod.housefinder.dataobjects.House
import com.jacoblip.andriod.housefinder.googlemaps.GeoFinder
import kotlinx.android.synthetic.main.fragmnet_house_details.*
import kotlinx.android.synthetic.main.fragmnet_house_details.view.*

class HouseDetailFragment(context: Context,house: House):Fragment() {

    var myContext = context
    var house = house
    lateinit var houseIV:ImageView
    lateinit var titleTV:TextView
    lateinit var addressTV:TextView
    lateinit var priceTV:TextView
    lateinit var navagationButton:Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragmnet_house_details,container,false)
        houseIV = view.findViewById(R.id.houseDetailsImage)
        titleTV = view.findViewById(R.id.houseDetailsTitle)
        addressTV = view.findViewById(R.id.houseDetailsAddress)
        priceTV = view.findViewById(R.id.houseDetailsPrice)
        navagationButton = view.findViewById(R.id.navagationButton)

        titleTV.text = house.title
        addressTV.text = house.address
        priceTV.text = house.price
        titleTV.setOnClickListener {  }
        addressTV.setOnClickListener {  }
        priceTV.setOnClickListener {  }
        houseIV.setOnClickListener {  }

        Glide.with(this).load(house.image).into(houseIV)

        navagationButton.setOnClickListener {
            val address = GeoFinder.getLocationFromAddress(context,house.address)
            if(address!=null) {
                var urlWaze = "https://waze.com/ul?ll=" + address.latitude + "," + address.longitude
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(urlWaze)
                )
                startActivity(intent)
            }else{
                Toast.makeText(context,"Sorry, the location given is in incorrect form",Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    companion object{
        fun newInstance(context: Context,house: House): HouseDetailFragment {
            return HouseDetailFragment(context,house)
        }
    }
}