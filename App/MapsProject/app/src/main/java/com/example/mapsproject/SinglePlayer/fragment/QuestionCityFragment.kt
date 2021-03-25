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

class QuestionCityFragment: Fragment(), View.OnClickListener {



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
        val editor = sharedPref?.edit()
        var sets = sharedPref?.getInt(getString(R.string.sets_key), 1)

        val city = sharedPref?.getString("city","").toString()
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

        var ctr = 0
        for (ctr in 0..shuffled.size - 1) {
            val btn = btnIter.next()
            val coun = citiesIter.next()

            btn.setText(coun)
            btn.setOnClickListener { view ->
                if (ctr == 0) {
                    SinglePlayerServerConf.score += 1
                    btn.setBackgroundColor(Color.GREEN)
                } else {
                    btn.setBackgroundColor(Color.RED)
                }


                SinglePlayerServerConf.level+=1

                Handler().postDelayed(
                        {
                            // This method will be executed once the timer is over

                            //if sets are not ended
                            if(SinglePlayerServerConf.level == SinglePlayerServerConf.sets){
                                findNavController().navigate(R.id.action_questionCityFragment_to_endFragment)
                            }else{
                                findNavController().navigate(R.id.action_questionCityFragment_to_btnFragment)
                            }
                        },
                        1000 // value in milliseconds
                )

            }


        }

        return rootView
    }


    override fun onClick(v: View?) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = sharedPref?.edit()
        var score = sharedPref?.getInt(getString(R.string.current_score_key), 0)
        var sets = sharedPref?.getInt(getString(R.string.sets_key), 1)

        Log.i("Mytag","sets: "+sets.toString())
        Log.i("Mytag","score: "+score.toString())

        when (v?.id) {
            R.id.button1 -> {
                v.setBackgroundColor(Color.GREEN)
                score = score?.plus(1)
                editor?.putInt((getString(R.string.current_score_key)), score!!)

            }

            R.id.button2 -> v.setBackgroundColor(Color.RED)
            R.id.button3 -> v.setBackgroundColor(Color.RED)
            R.id.button4 -> v.setBackgroundColor(Color.RED)
        }

        //update number of sets
        val nsets = sets!! -1
        editor?.putInt(getString(R.string.sets_key),nsets)
        editor?.apply()

        Handler().postDelayed(
                {
                    // This method will be executed once the timer is over

                    //if sets are not ended
                    if(nsets == 0){
                        findNavController().navigate(R.id.action_questionCityFragment_to_endFragment)
                    }else{
                        findNavController().navigate(R.id.action_questionCityFragment_to_btnFragment)
                    }
                },
                1000 // value in milliseconds
        )

    }
}