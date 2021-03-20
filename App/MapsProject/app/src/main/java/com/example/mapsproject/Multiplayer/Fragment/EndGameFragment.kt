package com.example.mapsproject.Multiplayer.Fragment

import android.content.Intent
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
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.score
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity

class EndGameFragment: Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        MultiPlayerServerConf.randomVar=0

        val rootView = inflater.inflate(R.layout.fragment_game_completed_mp, container, false)

        //rootView.findViewById<TextView>(R.id.finalScore).setVisibility(View.GONE)
        var textString = ""
        var maxPt=0
        var winner: MutableList<String> = mutableListOf()
        //rootView.findViewById<TextView>(R.id.finalScore).setText(textString)
        var i =0
        while (i < MultiPlayerServerConf.totalScore.length() ){
            textString = textString+"PLAYER "+i+": "+MultiPlayerServerConf.totalScore[i.toString()]+" PT\n"

            if(maxPt <  MultiPlayerServerConf.totalScore[i.toString()].toString().toInt()){
                winner.clear()
                winner.add(i.toString())
                maxPt= MultiPlayerServerConf.totalScore[i.toString()].toString().toInt()
            }
            else if(maxPt ==  MultiPlayerServerConf.totalScore[i.toString()].toString().toInt()){
                winner.add(i.toString())
                //maxPt= MultiPlayerServerConf.totalScore[i.toString()].toString().toInt()
            }
            i+=1
        }

        rootView.findViewById<TextView>(R.id.finalScore).setText(textString)
        var count =0
        rootView.findViewById<Button>(R.id.end_game).setOnClickListener { view ->
            if(count==0){
                count+=1
                var j=0
                rootView.findViewById<TextView>(R.id.title_end_game).setText("WINNER IS")
                textString=""
                while (j < winner.size){
                    textString+= "PLAYER "+winner.get(j)+"\n"
                    j+=1
                }
                textString +=""+maxPt+" PT\n"
                rootView.findViewById<TextView>(R.id.finalScore).setText(textString)
                rootView.findViewById<TextView>(R.id.end_game).setText("END GAME")
            }
            else{
                //val mRunnable ={
                    val i = Intent(activity, StartGameActivity::class.java)
                    startActivity(i)
                //}
                //val mHandler = Handler()
                //mHandler.postDelayed(mRunnable, 5000)
            }
        }

        return rootView
    }
}