package com.jacoblip.andriod.housefinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacoblip.andriod.housefinder.R
import com.jacoblip.andriod.housefinder.UI.fragments.MapAndRVFragment
import com.jacoblip.andriod.housefinder.UI.fragments.RecyclerViewHousesFragment
import com.jacoblip.andriod.housefinder.dataobjects.House
import kotlinx.android.synthetic.main.house_result_rv_item.view.*

class HouseResultsAdapter(houses: Array<House>,callbacks: RecyclerViewHousesFragment.Callbacks) :
    RecyclerView.Adapter<HouseResultsViewHolder>() {
    var houses = houses
    var callbacks = callbacks

    private val differCallback = object : DiffUtil.ItemCallback<House>() {
        override fun areItemsTheSame(oldItem: House, newItem: House): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: House, newItem: House): Boolean {
            return oldItem.equals(newItem)
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HouseResultsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.house_result_rv_item, parent, false)
        return HouseResultsViewHolder(view)
    }

    override fun getItemCount() = houses.size

    override fun onBindViewHolder(holder:HouseResultsViewHolder, position: Int) {
        var house = houses[position]
        holder.itemView.apply {
            Glide.with(this).load(house.image).into(houseItemImageView)
            houseResultTitleTV.text = house.title
            houseResultAddressTV.text = house.address
            houseResultPriceTV.text = house.price
            setOnClickListener {
                callbacks!!.onHouseSelected(house)
            }
        }
    }
}