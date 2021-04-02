package com.eneskiziltas.zeytin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.eneskiziltas.zeytin.adapter.CustomInfoWindowForGoogleMapAdapter
import com.eneskiziltas.zeytin.model.Locations
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_maps2.*
import kotlinx.android.synthetic.main.toolbar.*


class MapsFragment : Fragment() {

    var userLocation = LatLng(41.0268809, 29.0160583)
    private lateinit var mMap : GoogleMap
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener


    private val callback = OnMapReadyCallback { googleMap ->


        // Google map initialize
        mMap = googleMap


        //Map`in styleını yükleme
        try {

            val success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            requireActivity(), R.raw.map_style))
            if (!success) {

                Log.e("MapsActivityRaw", "style yüklendi.")
            }
        } catch (e: Resources.NotFoundException) {

            Log.e("MapsActivityRaw", "map style dosyası bulunamadı.", e)
        }
        

        // Deneme yapmak için dummy data
        val konum1ltlng = LatLng(41.0269019, 29.0155067)
        val konum2ltlng = LatLng(41.0266133, 29.014962)
        val konum3ltlng = LatLng(41.026010, 29.014668)
        val konum1 = Locations(konum1ltlng, 83, "Q3RSWO423")
        val konum2 = Locations(konum2ltlng, 76, "E2DKS238A")
        val konum3 = Locations(konum3ltlng, 15, "DA2D54A5")

        val konumList = ArrayList<Locations>()
        konumList.add(konum1)
        konumList.add(konum2)
        konumList.add(konum3)

        var i = 0
        while (i < konumList.size){
            googleMap.addMarker(MarkerOptions().position(konumList[i].latLng).title("Sarj : %" + konumList[i].sarj + "").icon(BitmapDescriptorFactory.fromResource(R.drawable.location_zeytin)))
            i++
        }


        //Marker Adapter`ı kendi hazırladığımız adapter sınıfı ile değiştiriyoruz
        mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMapAdapter(requireContext(), userLocation))

       //harita trafik durumu
        mMap.isTrafficEnabled = true

        mMap.isBuildingsEnabled = false

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f))


    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_maps2, container, false)

    }


    //Main
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        //View objelerinin fontlarını değiştirme
        custom_title.typeface = ResourcesCompat.getFont(requireContext(), R.font.alpha)
        button1.typeface = ResourcesCompat.getFont(requireContext(), R.font.alpha)


        //Toolbarı şişirme
        toolbar.inflateMenu(R.menu.menu)

       //Kullanıcının bulunduğu konuma marker koyma
       val markerOptions = MarkerOptions().title("Şu an buradasın").icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location_icon))


        var userMarker : Marker? = null

        //LocationManager initialize
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //LocationListener initialize
        locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location) {

                //Kullanıcının lokasyonu değiştikçe yeni marker atama işlemi

                userLocation.let {

                    userMarker?.remove()
                    userLocation = LatLng(location.latitude, location.longitude)


                    if (userLocation != null){

                        markerOptions.position(userLocation!!)
                        userMarker = mMap.addMarker(markerOptions)

                    }

                }

            }
        }




        //Kullanıcının konumunu almak için izin işlemleri
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)

        }else{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            val lastLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocationGPS != null){
                println("deneme0")
                userLocation = LatLng(lastLocationGPS.latitude, lastLocationGPS.longitude)
                System.out.println(userLocation.latitude)
                println("araba")
               // mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMapAdapter(requireContext(),userLocation))


                println("zommend")


            }
        }


        //Kullanıcının konumuna gitme buttonu listenerı
        myLocation.setOnClickListener {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f))
        }





    }


    //Kullanıcıdan izin istendikten sonra dönen veriye göre ne yapılacağının işlemleri
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(grantResults.size>0){
            println("grand")
            if (requestCode == 1){
                println("request")
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    println("self")
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    lastLocation.let {
                        userLocation = LatLng(it!!.latitude, it!!.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f))
                    }
                }
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    //Menü işlemleri
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }




}


