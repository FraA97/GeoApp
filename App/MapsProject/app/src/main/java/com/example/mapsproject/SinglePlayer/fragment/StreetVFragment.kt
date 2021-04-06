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
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng

class StreetVFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val randomLat= floatArrayOf(0.007f,-0.007f)
        val randomLong= floatArrayOf(0.01f,-0.01f)
        val lat = sharedPref?.getFloat("lat",0f)?.plus(randomLat[(0..1).random()]*Math.random())
        val long = sharedPref?.getFloat("long",0f)?.plus(randomLong[(0..1).random()]*Math.random())
        Log.d("myTag", "lat: "+lat+", long: "+long);
        val location = LatLng(lat!!.toDouble(), long!!.toDouble())

        val rootView: View = inflater.inflate(R.layout.fragment_streetv, container, false)
        val clocktext: TextView = rootView.findViewById<TextView>(R.id.clock_text_view)

        val streetViewPanoramaFragment =
                getChildFragmentManager()?.findFragmentById(R.id.streetviewpanorama) as SupportStreetViewPanoramaFragment?
        streetViewPanoramaFragment?.getStreetViewPanoramaAsync { panorama ->
            //lat: 1 grado = 111 km => 0.007 gradi = 800 metri
            //long:1 grado = 84 km=> 0.01 gradi = 800 metri
            savedInstanceState ?: panorama.setPosition(location,1000)
            savedInstanceState ?: panorama.setStreetNamesEnabled(false)
            savedInstanceState ?: panorama.setUserNavigationEnabled(false)
            savedInstanceState ?: panorama.setZoomGesturesEnabled(false)
            //savedInstanceState ?: panorama.setPanningGesturesEnabled(false)

            Log.i("info","gestures: "+panorama.isPanningGesturesEnabled.toString())
            Log.i("info","street names: "+panorama.isStreetNamesEnabled.toString())
            Log.i("info","navigation: "+panorama.isUserNavigationEnabled.toString())
            Log.i("info","zoom: "+panorama.isZoomGesturesEnabled.toString())

        }
        streetViewPanoramaFragment?.getStreetViewPanoramaAsync {
            panorama ->
            print("---------------------------------------------"+panorama.isPanningGesturesEnabled)
        }

        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                clocktext.setText(""+millisUntilFinished /1000);
            }

            override fun onFinish() {

                findNavController().navigate(R.id.action_StreetVFragment_to_questionCityFragment)

            }
        }.start()
        return rootView
    }
}