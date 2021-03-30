package com.example.mapsproject.Multiplayer.Fragment

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
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StreetVFragmentMP: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(context)
                        .setTitle(getString(R.string.title_back_press))
                        .setMessage(R.string.msg_back_press)
                        .setPositiveButton(android.R.string.yes) { dialog, which ->
                            (activity as MultiplayerActivity).interruptGame()
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
        val lat = sharedPref?.getFloat("lat",0f)
        val long = sharedPref?.getFloat("long",0f)
        Log.d("myTag", "lat: "+lat+", long: "+long);
        val location = LatLng(lat!!.toDouble(), long!!.toDouble())

        val rootView: View = inflater.inflate(R.layout.fragment_streetv, container, false)
        val clocktext: TextView = rootView.findViewById<TextView>(R.id.clock_text_view)

        val streetViewPanoramaFragment =
                getChildFragmentManager()?.findFragmentById(R.id.streetviewpanorama) as SupportStreetViewPanoramaFragment?
        streetViewPanoramaFragment?.getStreetViewPanoramaAsync { panorama ->

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

                findNavController().navigate(R.id.action_StreetVFragmentMP_to_questionCityFragmentMP)

            }
        }.start()
        return rootView
    }


}