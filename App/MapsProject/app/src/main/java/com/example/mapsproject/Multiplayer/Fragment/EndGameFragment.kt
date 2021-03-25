package com.example.mapsproject.Multiplayer.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.totalScore
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
        //var i =0
        val keys: Iterator<String> = totalScore.keys()
        while (keys.hasNext() ){
            val key = keys.next();
            textString = textString+"PLAYER "+key+": "+totalScore[key]+" PT\n"

            if(maxPt <  totalScore[key].toString().toInt()){
                winner.clear()
                winner.add(key)
                maxPt= totalScore[key].toString().toInt()
            }
            else if(maxPt ==  totalScore[key].toString().toInt()){
                winner.add(key)
                //maxPt= MultiPlayerServerConf.totalScore[i.toString()].toString().toInt()
            }
            //i+=1
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
                    textString+= ""+winner.get(j)+"\n"
                    j+=1
                }
                textString +=""+maxPt+" PT\n"
                rootView.findViewById<TextView>(R.id.finalScore).setText(textString)
                //rootView.findViewById<TextView>(R.id.finalScore).setTextSize(50, )
                rootView.findViewById<TextView>(R.id.end_game).text = "END GAME"
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