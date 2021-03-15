package com.example.mapsproject.Multiplayer.Fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.score
import com.example.mapsproject.R

class QuestionCityFragmentMP: Fragment(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MultiPlayerServerConf.queue = Volley.newRequestQueue(context)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val rootView: View = inflater.inflate(R.layout.fragment_questions, container, false)

        val btn1 = rootView.findViewById<Button>(R.id.button1)
        val btn2 = rootView.findViewById<Button>(R.id.button2)
        val btn3 = rootView.findViewById<Button>(R.id.button3)
        val btn4 = rootView.findViewById<Button>(R.id.button4)

        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = sharedPref?.edit()

        btn1.setText(sharedPref?.getString("city","").toString())
        //sharedPref?.getString("city","").toString()

        btn2.setText(sharedPref?.getString("fCity1", "").toString())
        //sharedPref?.getString("fCity1", "")
        btn3.setText(sharedPref?.getString("fCity2", "").toString())
        //sharedPref?.getString("fCity2", "")
        btn4.setText(sharedPref?.getString("fCity3", "").toString())
        //sharedPref?.getString("fCity3", "")

        return rootView
    }


    override fun onClick(v: View?) {

        Log.i("Mytag","score: "+score.toString())

        when (v?.id) {
            R.id.button1 -> {
                v.setBackgroundColor(Color.GREEN)
                score = score?.plus(1)

            }

            R.id.button2 -> v.setBackgroundColor(Color.RED)
            R.id.button3 -> v.setBackgroundColor(Color.RED)
            R.id.button4 -> v.setBackgroundColor(Color.RED)
        }



        Handler().postDelayed(
                {
                    // This method will be executed once the timer is over
                    findNavController().navigate(R.id.action_questionCityFragmentMP_to_finishLevelFragment)

                },
                1000 // value in milliseconds
        )

    }
}