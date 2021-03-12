package com.example.mapsproject.Multiplayer.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.R

class JoinFragment: Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_join, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.join_id_btn).setOnClickListener { view ->
            findNavController().navigate(R.id.action_joinFragment_to_joinIDFragment)

        }

        view.findViewById<Button>(R.id.random_join_btn).setOnClickListener { view ->
            findNavController().navigate(R.id.action_joinFragment_to_joinRandomFragment)

        }
    }
}