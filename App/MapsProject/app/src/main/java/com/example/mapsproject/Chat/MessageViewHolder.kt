package com.example.mapsproject.Chat

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsproject.Account.Account
import com.example.mapsproject.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class MessageViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {


    val storageRef = Firebase.storage.reference

    var messageTextView: TextView
    var messengerImageView: ImageView
    var messengerTextView: TextView
    var messageDate : TextView


    fun bindMessage(friendlyMessage: ChatMessage) {
        if (friendlyMessage.text != null) {
            messageTextView.setText(friendlyMessage.text)
            messengerTextView.setText(friendlyMessage.name)

            if(friendlyMessage.timestamp != null){
                messageDate.setText(friendlyMessage.timestamp.toString())
            }

            val pathCloud : String = "images/"+friendlyMessage.photoUrl
            val imagesRef = storageRef.child(pathCloud)

            val localFile = File.createTempFile("images", "jpg")
            imagesRef?.getFile(localFile)?.addOnSuccessListener {
                // Local temp file has been created
                messengerImageView.setImageBitmap((BitmapFactory.decodeFile(localFile.path)))
            }?.addOnFailureListener {

                null
            }
            messageTextView.setVisibility(TextView.VISIBLE);
        }
    }

    init {
        messageTextView = v?.findViewById<View>(R.id.messageTextView) as TextView
        messengerImageView = v?.findViewById<View>(R.id.messengerImageView) as ImageView
        messengerTextView = v?.findViewById<View>(R.id.messengerTextView) as TextView
        messageDate = v?.findViewById<View>(R.id.msg_date) as TextView
    }
}