package com.example.mapsproject.Multiplayer.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R

class NewGameFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_new_game_create, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var num_l =  view.findViewById<EditText>(R.id.levels_input).getText().toString()
        var editText = view.findViewById<EditText>(R.id.levels_input)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                num_l = view.findViewById<EditText>(R.id.levels_input).getText().toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        view.findViewById<Button>(R.id.create_btn).setOnClickListener { view->
            Log.i("MyTag", "text_num_lev: " + num_l)
            if(num_l==""){
            }
            else{
                MultiPlayerServerConf.levels = num_l.toInt()
                findNavController().navigate(R.id.action_newGameFragment_to_waitingNewGameFragment)
            }
        }
    }


}