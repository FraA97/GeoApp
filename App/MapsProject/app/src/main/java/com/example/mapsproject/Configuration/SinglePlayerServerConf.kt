package com.example.mapsproject.Configuration

class SinglePlayerServerConf {
    companion object{
        //var url = "http://10.0.2.2:5000/?"
        var url = "https://marcocerino.pythonanywhere.com/?"

        val pollingPeriod :Long = 2000L

        val FirstReq = 0
        val SecondReq = 1

        var score = 0

        var sets = 3
        var level = 0

        var soundOn = true


        var canPoll=false
        //python3 MyMultiplayerServer.py
    }
}