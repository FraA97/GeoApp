package com.example.mapsproject.Configuration

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MultiPlayerServerConf {
    companion object{
        //var url = "http://fraart.pythonanywhere.com/?"
        var url = "http://10.0.2.2:8080/?"

        val pollingPeriod :Long = 2000L

        val startGameReq = 0
        val waitPlayerReq = 1
        val startLevelReq = 2
        val finishLevelReq = 3
        val waitLevelReq = 4
        val interruptGameReq = 5

        var randomVar=0
        var queue : RequestQueue? = null

        var game_id=0
        var player_id = 0

        var levels = 3
        var played_levels = 0

        var score = 0
        var totalScore=JSONObject()

        var num_players = 1
        var name_players = ""
        var soundOn = true
        var language = "en"
        var canPoll=false
        var wantToPlay = false
    }
}