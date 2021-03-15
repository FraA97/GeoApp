package com.example.mapsproject.Configuration

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MultiPlayerServerConf {
    companion object{
        //var url = "http://fraart.pythonanywhere.com/?"
        var url = "http://10.0.2.2:8080/?"
        //val clientid="241824565470-884uiqtb5glg4ec4d4l9nfg0el6ac18s.apps.googleusercontent.com"

        val pollingPeriod :Long = 2000L

        val startGameReq = 0
        val waitPlayerReq = 1
        val startLevelReq = 2
        val finishLevelReq = 3
        val waitLevelReq = 4

        var queue : RequestQueue? = null

        var game_id=0
        var player_id = 0

        var levels = 3
        var played_levels = 0

        var score = 0

        var canPoll=false
    }
}