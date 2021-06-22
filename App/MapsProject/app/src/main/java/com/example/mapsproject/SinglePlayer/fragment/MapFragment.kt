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
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.R
import com.example.mapsproject.SinglePlayer.SingleplayerActivity
import com.example.mapsproject.StartGameActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private lateinit var mMap: GoogleMap
    lateinit var rootView:View

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
                        // finish()
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
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fr) as SupportMapFragment
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

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onStop(){
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val lat = sharedPref?.getFloat("lat", 0f)
        val long = sharedPref?.getFloat("long", 0f)
        Log.d("myTag", "lat: " + lat + ", long: " + long);



        mMap = googleMap
        mMap.setOnMapLoadedCallback(this)


        val location = LatLng(lat!!.toDouble(), long!!.toDouble())

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        mMap.setMinZoomPreference(13.0f)
        mMap.addMarker(MarkerOptions().position(location))
        val cameraPosition = CameraPosition.Builder().target(location).zoom(14.0f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap.moveCamera(cameraUpdate)


    }

    override fun onMapLoaded() {
        val clocktext:TextView = rootView.findViewById<TextView>(R.id.clock_text_view)
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                clocktext.setText("" + millisUntilFinished / 1000);
            }

            override fun onFinish() {

                if(activity!=null) findNavController().navigate(R.id.action_mapFragment_to_optionsFragment)

            }
        }.start()

    }


}