package com.example.varina_puhelimeen

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class DelayService : Service() {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val channelId = "delay_channel"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createChannel()

        // Foreground notification so Android won't kill the service
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Trigger active")
            .setContentText("Vibration starts in 5 minutes…")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .build()

        startForeground(1, notification)

        scope.launch {
            delay(5 * 60 * 1000)  // 5 minutes
            vibratePattern()
            stopSelf()
        }

        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibratePattern() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Repeating vibration pattern for 5 seconds
        val pattern = longArrayOf(
            0,   400, 200,
            400, 200,
            400, 200,
            400, 200,
            400
        )

        vibrator.vibrate(
            VibrationEffect.createWaveform(pattern, -1)
        )
    }

    //adding a channel that is used to perform the functionality
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Delay Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?) = null
}