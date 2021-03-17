package com.example.testmapv1


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import kotlin.math.log

/**
 * This shows how to create a simple activity with streetview
 */
class MapsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.street_view_panorama_basic_demo)

        val streetViewPanoramaFragment =
            supportFragmentManager.findFragmentById(R.id.streetviewpanorama) as SupportStreetViewPanoramaFragment?

        streetViewPanoramaFragment?.getStreetViewPanoramaAsync { panorama ->
            // Only set the panorama to SYDNEY on startup (when no panoramas have been
            // loaded which is when the savedInstanceState is null).
            savedInstanceState ?: panorama.setPosition(LOCATION,50)
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
    }

    companion object {
        // George St, Sydney
        private val LOCATION = LatLng(33.34058, 44.40088)
    }
}