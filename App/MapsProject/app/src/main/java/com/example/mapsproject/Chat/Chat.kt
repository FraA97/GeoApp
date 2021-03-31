package com.example.mapsproject.Chat

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsproject.Account.Account.getUserName
import com.example.mapsproject.Account.Account.getUserPhotoUrl
import com.example.mapsproject.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase


class Chat: Activity() {
    val MESSAGES_CHILD = "messages"


    val mDatabase = FirebaseDatabase.getInstance()
    var messagesRef = mDatabase.reference.child(MESSAGES_CHILD)
    var options: FirebaseRecyclerOptions<ChatMessage> =
        FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(messagesRef, ChatMessage::class.java)
            .build()


    val mFirebaseAdapter = object:  FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
           val inflater= LayoutInflater.from(parent.context)
            return MessageViewHolder(inflater.inflate(R.layout.element_chat_msg, parent, false))
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: ChatMessage) {
            holder.bindMessage(model)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        //set visibility of send btn
        findViewById<EditText>(R.id.message_input).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().trim { it <= ' ' }.length > 0) {
                    findViewById<FloatingActionButton>(R.id.fab).setEnabled(true)
                } else {
                    findViewById<FloatingActionButton>(R.id.fab).setEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        //send new message
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view->
            val msg = ChatMessage(
                findViewById<EditText>(R.id.message_input).text.toString(),
                getUserName(), getUserPhotoUrl()
            )
            messagesRef.push().setValue(msg)
            findViewById<EditText>(R.id.message_input).setText("")
        }


        val mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.setStackFromEnd(true)
        val recyclerView = findViewById<RecyclerView>(R.id.list_of_messages)
        recyclerView.setLayoutManager(mLinearLayoutManager)
        recyclerView.setAdapter(mFirebaseAdapter)


        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver for details
        mFirebaseAdapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(
                recyclerView,
                mFirebaseAdapter,
                mLinearLayoutManager
            )
        )
    }
    override fun onPause() {
        mFirebaseAdapter?.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAdapter?.startListening()
    }
}