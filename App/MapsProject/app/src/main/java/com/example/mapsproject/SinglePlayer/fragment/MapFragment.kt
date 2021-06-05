package com.example.mapsproject.SinglePlayer.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.SinglePlayer.SingleplayerActivity
import com.example.mapsproject.StartGameActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment: Fragment(),OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{
    private  var mMap: GoogleMap?=null
    lateinit var rootView:View
    lateinit var mapFragment:SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as SingleplayerActivity).findViewById<Button>(R.id.ready_button_sp).setVisibility(
            View.INVISIBLE
        )
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.title_back_press))
                    .setMessage(R.string.msg_back_press)
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        val i = Intent(activity, StartGameActivity::class.java)
                        (activity as SingleplayerActivity).finish()
                        startActivity(i)
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("myTag","MapFragment1; level: "+SinglePlayerServerConf.level)
        rootView = inflater.inflate(R.layout.fragment_map, container, false)

        //update level text

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val lat = sharedPref?.getFloat("lat", 0f)
        val long = sharedPref?.getFloat("long", 0f)
        Log.d("myTag", "lat: " + lat + ", long: " + long)

        val location = LatLng(lat!!.toDouble(), long!!.toDouble())

        val cameraPosition = CameraPosition.Builder().target(location).zoom(14.0f).build()
        val option = GoogleMapOptions().camera(cameraPosition).useViewLifecycleInFragment(true)

        mapFragment = SupportMapFragment.newInstance(option)

        childFragmentManager.beginTransaction().add(R.id.map_container,mapFragment,"com.example.map_fragment").commit()

        mapFragment.getMapAsync(this)
        return rootView
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onStop(){
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (googleMap!= null) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
            val lat = sharedPref?.getFloat("lat", 0f)
            val long = sharedPref?.getFloat("long", 0f)
            Log.d("myTag", "lat: " + lat + ", long: " + long)



            mMap = googleMap
            mMap!!.setOnMapLoadedCallback(this)


            val location = LatLng(lat!!.toDouble(), long!!.toDouble())

            mMap!!.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
            mMap!!.addMarker(MarkerOptions().position(location))
            val clocktext:TextView = rootView.findViewById<TextView>(R.id.clock_text_view)
            object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    clocktext.setText( (millisUntilFinished / 1000).toString())
                }

                override fun onFinish() {

                    if(activity!=null) findNavController().navigate(R.id.action_mapFragment_to_optionsFragment)
                }
            }.start()
        }
    }

    override fun onMapLoaded() {

        /* val clocktext: TextView = rootView.findViewById<TextView>(R.id.clock_text_view)

             object : CountDownTimer(10000, 1000) {
                 override fun onTick(millisUntilFinished: Long) {
                     clocktext.setText(""+millisUntilFinished /1000);
                 }

                 override fun onFinish() {

                     findNavController().navigate(R.id.action_mapFragmentMP_to_questionCountryFragmentMP)

                 }
             }.start()
               */
    }


}