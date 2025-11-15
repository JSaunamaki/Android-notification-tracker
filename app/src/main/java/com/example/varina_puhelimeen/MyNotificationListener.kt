package com.example.varina_puhelimeen

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import kotlin.jvm.java

class MyNotificationListener : NotificationListenerService() {

    //searched keywords from notification
    private val keywords = listOf("SEMMA", "2,95€", "3,65€", "3,95€")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val text = extras.getCharSequence("android.text")?.toString() ?: ""
        val title = extras.getCharSequence("android.title")?.toString() ?: ""

        val content = "$title $text"


        //the performing part of the code
        if (keywords.any { content.contains(it, ignoreCase = true) }) {
            val intent = Intent(this, DelayService::class.java)
            startForegroundService(intent)
        }
    }
}