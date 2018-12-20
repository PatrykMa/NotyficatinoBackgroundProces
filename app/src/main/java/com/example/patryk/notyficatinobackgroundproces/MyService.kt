package com.example.patryk.notyficatinobackgroundproces

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import android.media.session.PlaybackState.ACTION_PAUSE
import android.media.session.PlaybackState.ACTION_PLAY
import android.util.Log
import android.support.v4.app.NotificationCompat
import android.media.session.PlaybackState.ACTION_PAUSE
import android.media.session.PlaybackState.ACTION_PLAY
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Build
import java.lang.Exception


class MyService:Service() {
    companion object {
        val ACTION_START_FOREGROUND_SERVICE="start"
        val ACTION_STOP_FOREGROUND_SERVICE = "stop"
        val ACTION_PLAY = "play"
        val ACTION_PAUSE = "pause"
    }
    val CHANNEL_ID="com.example.music"

    var player:MediaPlayer?=null
    var time=0

    override fun onBind(intent: Intent?): IBinder? {
        return  null
    }

    fun initPlayer()
    {
        if(player==null) {
            player = MediaPlayer.create(this, R.raw.sound).apply {
                this.isLooping = true
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            when (action) {
                ACTION_START_FOREGROUND_SERVICE -> {
                    startForegroundService()
                    createNotificationChannel()
                    initPlayer()
                    Toast.makeText(applicationContext, "Foreground service is started.", Toast.LENGTH_LONG).show()
                }
                ACTION_STOP_FOREGROUND_SERVICE -> {
                    stopForegroundService()
                    Toast.makeText(applicationContext, "Foreground service is stopped.", Toast.LENGTH_LONG).show()
                }
                ACTION_PLAY -> {Toast.makeText(applicationContext, "You click Play button.", Toast.LENGTH_LONG).show()
                initPlayer()
                    player?.seekTo(time)
                    player?.start()
                }
                ACTION_PAUSE -> {
                    Toast.makeText(applicationContext, "You click Pause button.", Toast.LENGTH_LONG).show()
                    time= try{player!!.currentPosition} catch (e:Exception) {0}
                    player?.stop()
                    player=null
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopForegroundService() {
        Log.d("servis", "Stop foreground service.")
        player?.stop()
        // Stop foreground service and remove the notification.
        stopForeground(true)

        // Stop the foreground service.
        stopSelf()
    }

    fun startForegroundService()
    {
        Log.d("servis", "Start foreground service.")
        //createIntent To play
        val playIntent = Intent(this, MyService::class.java)
        playIntent.action = ACTION_PLAY
        val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
        // pause
        val pauseIntent = Intent(this, MyService::class.java)
        playIntent.action = ACTION_PAUSE
        val pendingPauseIntent = PendingIntent.getService(this, 0, playIntent, 0)
        //stop
        val stopIntent = Intent(this, MyService::class.java)
        playIntent.action = ACTION_STOP_FOREGROUND_SERVICE
        val pendingStopIntent = PendingIntent.getService(this, 0, playIntent, 0)

        var mBuilder = NotificationCompat.Builder(this, "chanel")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Tytuł powiadomienia")
            .setContentText("bardzo dlugi tekst który sam wymysliłem i zawiera masę literówek i błędów ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_stat_name,"Play",pendingPlayIntent)
            .addAction(R.drawable.ic_stat_name,"Pause",pendingPauseIntent)
            .addAction(R.drawable.ic_stat_name,"Stop",pendingStopIntent).build()

        startForeground(1, mBuilder)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}