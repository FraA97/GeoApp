package com.example.mapsproject.SinglePlayer.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment: Fragment(), OnMapReadyCallback {
    var mMapView: MapView? = null
    lateinit var rootView:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mMapView = rootView.findViewById<View>(R.id.map) as MapView
        mMapView!!.onCreate(savedInstanceState)

        mMapView!!.onResume() // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMapView!!.getMapAsync(this)

        return rootView
    }


    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val lat = sharedPref?.getFloat("lat",0f)
        val long = sharedPref?.getFloat("long",0f)
        Log.d("myTag", "lat: "+lat+", long: "+long);

        val clocktext:TextView = rootView.findViewById<TextView>(R.id.clock_text_view)


        googleMap!!.setMapType(GoogleMap.MAP_TYPE_SATELLITE)



        val location = LatLng(lat!!.toDouble(), long!!.toDouble())
        googleMap.addMarker(MarkerOptions().position(location).title("Marker "))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
        googleMap.getUiSettings().setZoomControlsEnabled(false);


        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                clocktext.setText(""+millisUntilFinished /1000);
            }

            override fun onFinish() {

                findNavController().navigate(R.id.action_mapFragment_to_optionsFragment)

            }
        }.start()
    }


}