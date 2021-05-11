package com.example.mapsproject.SinglePlayer.fragment

import android.app.AlertDialog
import android.content.Intent
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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.SinglePlayer.SingleplayerActivity
import com.example.mapsproject.StartGameActivity

class QuestionCityFragment: Fragment(), View.OnClickListener {

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


        val rootView: View = inflater.inflate(R.layout.fragment_questions, container, false)

        val btn1 = rootView.findViewById<Button>(R.id.button1)
        val btn2 = rootView.findViewById<Button>(R.id.button2)
        val btn3 = rootView.findViewById<Button>(R.id.button3)
        val btn4 = rootView.findViewById<Button>(R.id.button4)
        val question_title = rootView.findViewById<TextView>(R.id.textView).setText(R.string.guess_the_city)

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
        val letters =  listOf<String>("A: ", "B: ", "C: ", "D:")

        var ctr = 0
        for (ctr in 0..shuffled.size - 1) {
            val btn = btnIter.next()
            val coun = citiesIter.next()

            val btnString=shuffled[ctr].toString()
            val btnId =btnString.subSequence(btnString.length-2,btnString.length-1).toString().toInt()
            btn.setText(letters[btnId-1]+coun)

            btn.setOnClickListener { view ->
                if (ctr == 0) {
                    SinglePlayerServerConf.score += 1
                    if(SinglePlayerServerConf.soundOn) {
                        val mysong = MediaPlayer.create(context, R.raw.risposta_esatta)
                        mysong.start()
                    }
                    btn.setBackgroundColor(Color.GREEN)
                    (activity as SingleplayerActivity).findViewById<TextView>(R.id.curr_score_sp).setText(SinglePlayerServerConf.score.toString()) //update visualization of score
                } else {
                    if(SinglePlayerServerConf.soundOn) {
                        val mysong = MediaPlayer.create(context, R.raw.risposta_sbagliata)
                        mysong.start()
                    }
                    btn.setBackgroundColor(Color.RED)

                }
                btn.setOnClickListener(null);

                SinglePlayerServerConf.level+=1

                Handler().postDelayed(
                        {
                            // This method will be executed once the timer is over

                            //if sets are not ended
                            if(SinglePlayerServerConf.level > SinglePlayerServerConf.sets){
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