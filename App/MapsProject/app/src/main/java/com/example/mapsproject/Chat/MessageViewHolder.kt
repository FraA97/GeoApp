package com.example.mapsproject.Chat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsproject.R


class MessageViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
    var messageTextView: TextView
    var messengerImageView: ImageView
    var messengerTextView: TextView

    fun bindMessage(friendlyMessage: ChatMessage) {
        if (friendlyMessage.text != null) {
            messageTextView.setText(friendlyMessage.text);
            messageTextView.setVisibility(TextView.VISIBLE);
        }
    }

    init {
        messageTextView = itemView.findViewById<View>(R.id.messageTextView) as TextView
        messengerImageView = itemView.findViewById<View>(R.id.messengerImageView) as ImageView
        messengerTextView = itemView.findViewById<View>(R.id.messengerTextView) as TextView
    }
}