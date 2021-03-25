package com.example.mapsproject.SinglePlayer.fragment

import android.graphics.Color
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
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R

class QuestionCountryFragment : Fragment() {


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
        val question_title = rootView.findViewById<TextView>(R.id.textView).setText("GUESS THE COUNTRY")

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
        val letters =  listOf<String>("A: ", "B: ", "C: ", "D:")
        var ctr = 0
        for (ctr in 0..shuffled.size - 1) {
            val btn = btnIter.next()
            val coun = ctrIter.next()
            val btnString=shuffled[ctr].toString()
            val btnId =btnString.subSequence(btnString.length-2,btnString.length-1).toString().toInt()

            btn.setText(letters[btnId-1]+coun)
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
                            findNavController().navigate(R.id.action_questionCountryFragment_to_streetVFragment)

                        },
                        1000 // value in milliseconds
                )
            }


        }


        return rootView
    }
}


