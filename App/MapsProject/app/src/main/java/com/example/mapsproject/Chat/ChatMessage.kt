package com.example.mapsproject.Chat

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat


class ChatMessage {
    var text: String? = null
    var name: String? = null
    var photoUrl: String? = null
    var timestamp: String? = null
    constructor() {}
    constructor(text: String?, name: String?, photoUrl: String?,timestamp: String?) {
        this.text = text
        this.name = name
        this.photoUrl = photoUrl
        this.timestamp = timestamp
    }
}