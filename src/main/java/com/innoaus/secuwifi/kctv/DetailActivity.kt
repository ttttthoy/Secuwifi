package com.innoaus.secuwifi.kctv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import com.innoaus.rainpass.sdk.RainpassWifi
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var textCode1: TextView
    private lateinit var textCode2: TextView
    private lateinit var textCountdown: TextView
    var oldcode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        findViewById<TextView>(R.id.text_title).text = "다른 기기에서 연결하기"
        findViewById<AppCompatImageButton>(R.id.toolbar_button_back).setOnClickListener { finish() }

        textCode1 = findViewById(R.id.detail_code1)
        textCode2 = findViewById(R.id.detail_code2)
        textCountdown = findViewById(R.id.detail_countdown)

        val filter = IntentFilter()
        filter.addAction(RainpassWifi.ACTION.TICKET_RECEIVED)
        filter.addAction(RainpassWifi.ACTION.TICKET_FAILED)
        registerReceiver(receiver, filter)
        Manager.instance().getWifiTicket(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == RainpassWifi.ACTION.TICKET_RECEIVED) {
                val ticket = Manager.instance().getTicket()
                if (ticket != null) {
                    val id = ticket.getString("id")
                    val password = ticket.getString("password")
                    val expiration = ticket.getString("expiration")
                    updateUI(id, password, expiration)
                }
            } else if (action == RainpassWifi.ACTION.TICKET_FAILED) {
            }
        }
    }

    fun updateUI(username: String, password: String, expiration: String) {
        textCode1.text = username
        textCode2.text = password
        textCountdown.text = expiration
    }

    private fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d("[Detail]", "## $message")
        }
    }
}
