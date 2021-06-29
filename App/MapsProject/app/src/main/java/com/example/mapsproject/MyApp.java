package com.example.mapsproject;

import android.app.Application;
import android.util.Log;

import com.example.mapsproject.Configuration.MultiPlayerServerConf;

import java.util.Locale;

public class MyApp extends Application {
    public MyApp() {
        // this method fires only once per application start.
        // getApplicationContext returns null here
        String currLang = Locale.getDefault().getDisplayLanguage();
        MultiPlayerServerConf.Companion.setLanguage(currLang.substring(0,2));
        Log.i("main", "LANGUAGE: "+MultiPlayerServerConf.Companion.getLanguage());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // this method fires once as well as constructor
        // but also application has context here

        Log.i("main", "onCreate fired");
    }

}
