package com.example.mapsproject.SinglePlayer.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf.Companion.SecondReq
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class OptionsFragment:Fragment() {
    var queue : RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(context)

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
        val rootView = inflater.inflate(R.layout.fragment_loading, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOptions()
        //  Handler(Looper.getMainLooper()).postDelayed({check()},Conf.pollingPeriod)
    }

    private fun getOptions() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        var city = sharedPref?.getString("city", "").toString()
        var country = sharedPref?.getString("country", "").toString()
        country = country.replace(" ","%20")
        city = city.replace(" ","%20")

        val l:Int
        if(SinglePlayerServerConf.level<=3)
            l=SinglePlayerServerConf.level
        else
            l=3

        Log.i("myTag", "request: " + SinglePlayerServerConf.url + "req=" + SecondReq + "&level="+l+"&country=" + country + "&city=" + city + "")
        val stringRequest = StringRequest(
                Request.Method.GET, SinglePlayerServerConf.url + "req=" + SecondReq +"&level="+l+ "&country=" + country + "&city=" + city + "", { response ->
            val reply = JSONObject(response.toString())
            val error = reply!!.getBoolean("error")
            if(!error){
                val fCountry1 = reply!!.get("fCountry1")
                val fCity1 = reply!!.get("fCity1")
                val fCountry2 = reply!!.get("fCountry2")
                val fCity2 = reply!!.get("fCity2")
                val fCountry3 = reply!!.get("fCountry3")
                val fCity3 = reply!!.get("fCity3")

                Log.i("myTag", "country: " + fCountry1 + ", city: " + fCity1 + "\n" +
                        "country: " + fCountry2 + ", city: " + fCity2 + "\n" +
                        "country: " + fCountry3 + ", city: " + fCity3)

                //save everything on resource
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = sharedPref?.edit()
                editor?.putString("fCountry1", fCountry1.toString())
                editor?.putString("fCity1", fCity1.toString())
                editor?.putString("fCountry2", fCountry2.toString())
                editor?.putString("fCity2", fCity2.toString())
                editor?.putString("fCountry3", fCountry3.toString())
                editor?.putString("fCity3", fCity3.toString())
                editor?.apply()

                findNavController().navigate(R.id.action_optionsFragment_to_questionCountryFragment)
            }
            else{
                val msg = reply!!.getString("msg")
                Toast.makeText(activity,"Error: "+msg,Toast.LENGTH_SHORT).show()
                Log.i("myTag", "Error: "+msg)
            }


        }, { error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
        })
        stringRequest.setRetryPolicy(DefaultRetryPolicy(
                20 * 1000,  //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue?.add(stringRequest)
    }


}