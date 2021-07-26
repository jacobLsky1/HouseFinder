package com.jacoblip.andriod.housefinder.googlemaps

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ClusterManagerRender(context: Context,map:GoogleMap,clusterManager: ClusterManager<ClusterItem>) :
    DefaultClusterRenderer<ClusterItem>(context,map,clusterManager) {

    var context = context
    var iconGenerator :IconGenerator = IconGenerator(context)
    var imageView: ImageView = ImageView(context)
    var map = map

    init {
        imageView.layoutParams = LinearLayout.LayoutParams(250,250)
        imageView.setPadding(5,5,5,5)
        iconGenerator.setContentView(imageView)
    }

    override fun onBeforeClusterItemRendered(item: ClusterItem?, markerOptions: MarkerOptions?) {
        if (item != null) {
            var image= item.house.image
            doAsync {
                var img: Bitmap? = Glide.with(context).asBitmap().load(image).submit().get()
            uiThread {
                iconGenerator.setContentView(imageView)
                imageView.setImageBitmap(img)
                var icon :Bitmap = iconGenerator.makeIcon()
                markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(icon))?.title(item.title)
                map.addMarker(markerOptions)
            }
            }

        }

    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterItem>?): Boolean {
        return false
    }
}