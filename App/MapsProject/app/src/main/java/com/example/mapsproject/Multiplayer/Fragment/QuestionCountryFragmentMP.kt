package com.example.mapsproject.Multiplayer.Fragment

//import com.example.mapsproject.Configuration.SinglePlayerServerConf

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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.score
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity


class QuestionCountryFragmentMP: Fragment() {



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
                            (activity as MultiplayerActivity).finish()
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
        val question_title = rootView.findViewById<TextView>(R.id.textView).setText(R.string.guess_the_country)

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
            val btnId =btnString.subSequence(btnString.length - 2, btnString.length - 1).toString().toInt()
            btn.setText(letters[btnId - 1] + coun)
            btn.setOnClickListener { view ->
                if (ctr == 0) {
                    score += 1
                    (activity as MultiplayerActivity).findViewById<TextView>(R.id.curr_score).setText(score.toString()) //update visualization of score
                    btn.setBackgroundColor(Color.GREEN)
                    if(SinglePlayerServerConf.soundOn) {
                        val mysong = MediaPlayer.create(context, R.raw.risposta_esatta)
                        mysong.start()
                    }
                } else {
                    if(SinglePlayerServerConf.soundOn) {
                        val mysong = MediaPlayer.create(context, R.raw.risposta_sbagliata)
                        mysong.start()
                    }
                    btn.setBackgroundColor(Color.RED)

                }
                btn.setOnClickListener(null);

                Handler().postDelayed(
                        {
                            // This method will be executed once the timer is over
                            findNavController().navigate(R.id.action_questionCountryFragmentMP_to_streetVFragmentMP)

                        },
                        1000 // value in milliseconds
                )

            }
        }

        return rootView
    }




}
