package com.example.mapsproject.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.R

class QuestionFragment:Fragment(), OnClickListener{

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
                         findNavController().navigate(R.id.action_questionFragment_to_endFragment)
                    }else{
                    findNavController().navigate(R.id.action_questionFragment_to_btnFragment)
                    }
                },
                1000 // value in milliseconds
        )

    }
}