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
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.score
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R

class QuestionCountryFragmentMP: Fragment() {


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

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)

        val country = sharedPref?.getString("country", "").toString()
        //sharedPref?.getString("city","").toString()

        val fCountry1 = sharedPref?.getString("fCountry1", "").toString()
        //sharedPref?.getString("fCity1", "")
        val fCountry2 = sharedPref?.getString("fCountry2", "").toString()
        //sharedPref?.getString("fCity2", "")
        val fCountry3 = sharedPref?.getString("fCountry3", "").toString()
        //sharedPref?.getString("fCity3", "")

        val buttons = listOf<Button>(btn1, btn2, btn3, btn4)
        val shuffled = buttons.shuffled()
        val btnIter = shuffled.listIterator()
        val countries = listOf<String>(country, fCountry1, fCountry2, fCountry3)
        val ctrIter = countries.listIterator()
        var ctr = 0
        for (ctr in 0..shuffled.size - 1) {
            val btn = btnIter.next()
            val coun = ctrIter.next()

            btn.setText(coun)
            btn.setOnClickListener { view ->
                if (ctr == 0) {
                    SinglePlayerServerConf.score += 1
                    btn.setBackgroundColor(Color.GREEN)
                } else {
                    btn.setBackgroundColor(Color.RED)
                }

                Handler().postDelayed(
                        {
                            // This method will be executed once the timer is over
                            findNavController().navigate(R.id.action_questionCountryFragmentMP_to_questionCityFragmentMP)

                        },
                        1000 // value in milliseconds
                )

            }
        }

        return rootView
    }


}
