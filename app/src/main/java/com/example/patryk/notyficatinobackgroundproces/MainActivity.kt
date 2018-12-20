package com.example.patryk.notyficatinobackgroundproces

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.Button
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent



class MainActivity : AppCompatActivity() {


    val CHANNEL_ID ="com.example.Normal"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // createNotificationChannel()
        findViewById<Button>(R.id.button_createNotification).setOnClickListener {


            val intent = Intent(this, MyService::class.java)
            intent.setAction(MyService.ACTION_START_FOREGROUND_SERVICE)
            startService(intent)
        }
    }

}
