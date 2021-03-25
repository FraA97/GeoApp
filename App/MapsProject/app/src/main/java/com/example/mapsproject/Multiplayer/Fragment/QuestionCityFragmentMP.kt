package com.example.mapsproject.Multiplayer.Fragment

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.score
import com.example.mapsproject.R

class QuestionCityFragmentMP: Fragment() {


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
        val question_title = rootView.findViewById<TextView>(R.id.textView).setText("GUESS THE CITY")


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)

        val city = sharedPref?.getString("city", "").toString()
        //sharedPref?.getString("city","").toString()

        val fCity1 = sharedPref?.getString("fCity1", "").toString()
        //sharedPref?.getString("fCity1", "")
        val fCity2 = sharedPref?.getString("fCity2", "").toString()
        //sharedPref?.getString("fCity2", "")
        val fCity3 = sharedPref?.getString("fCity3", "").toString()
        //sharedPref?.getString("fCity3", "")

        val buttons = listOf<Button>(btn1, btn2, btn3, btn4)
        val shuffled = buttons.shuffled()
        val btnIter = shuffled.listIterator()
        val cities = listOf<String>(city, fCity1, fCity2, fCity3)
        val citiesIter = cities.listIterator()
        val letters =  listOf<String>("A: ", "B: ", "C: ", "D:")

        for (ctr in 0..shuffled.size - 1) {
            val btn = btnIter.next()
            val coun = citiesIter.next()

            val btnString=shuffled[ctr].toString()
            val btnId =btnString.subSequence(btnString.length-2,btnString.length-1).toString().toInt()
            btn.setText(letters[btnId-1]+coun)

            btn.setOnClickListener { view ->
                if (ctr == 0) {
                    score += 1

                    val mysong = MediaPlayer.create(context,R.raw.risposta_esatta)
                    mysong.start()
                    btn.setBackgroundColor(Color.GREEN)
                } else {
                    val mysong = MediaPlayer.create(context,R.raw.risposta_sbagliata)
                    mysong.start()
                    btn.setBackgroundColor(Color.RED)
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

        return rootView
    }

}