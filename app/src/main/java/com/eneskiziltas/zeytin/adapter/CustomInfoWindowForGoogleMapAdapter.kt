package com.eneskiziltas.zeytin.adapter

import android.app.Activity
import android.content.Context
import android.location.Location
import android.view.View
import android.widget.TextView
import com.eneskiziltas.zeytin.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowForGoogleMapAdapter(context : Context, userLocations: LatLng) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    //Kendi hazırladığımız info window`u şişiriyoruz
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    //info windowda mesafeyi hesaplamak için tutucu değişkenler tanımlıyoruz
    var userLocation = userLocations
    var location = Location("")
    var location2 = Location("")



    private fun rendoWindowText(marker: Marker, view: View){


        //Her bir marker`ın içinin dizaynı

        location.latitude = marker.position.latitude
        location.longitude = marker.position.longitude

        System.out.println(marker.position.latitude)

        location2.latitude = userLocation.latitude
        location2.longitude = userLocation.longitude
        //Kullanıcının lokasyonunun markerlara uzaklığını hesaplıyoruz
        var distance = location2.distanceTo(location)

        val txtSarj = view.findViewById<TextView>(R.id.infoWindowText_Sarj)
        val txtUzaklık = view.findViewById<TextView>(R.id.infowWindowText_Uzaklık)
        val txtUcret = view.findViewById<TextView>(R.id.infoWindowText_Ucret)



      var di = distance.toInt()
        // 1000 metreden sonra KM şeklinde yazmdırma
        if (di >= 1000){
            di /= 1000
            txtUcret.text = "1.70 + 0.86/dk"
            txtSarj.text = marker.title
            txtUzaklık.text = di.toString()+" KM"
        }else{
            txtUcret.text = "1.70 + 0.86/dk"
            txtSarj.text = marker.title
            txtUzaklık.text = di.toString()+" metre"
        }

    }

    override fun getInfoWindow(p0: Marker?): View {

        p0.let {
            rendoWindowText(it!!, mWindow)

        }

        return mWindow

    }

    override fun getInfoContents(p0: Marker?): View {

        p0.let {
            rendoWindowText(it!!, mWindow)
            return mWindow
        }

    }


}