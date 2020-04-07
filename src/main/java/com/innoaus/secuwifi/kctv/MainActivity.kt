package com.innoaus.secuwifi.kctv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import android.os.Handler
import com.innoaus.rainpass.sdk.RainpassWifi

class MainActivity : AppCompatActivity() {
    lateinit var content1Layout: LinearLayout
    lateinit var content2Layout: LinearLayout
    lateinit var statusDefaultLayout: LinearLayout
    lateinit var statusDetailLayout: LinearLayout
    private val REQUEST_CODE_DETAIL = 6001

    lateinit var toolbarButton: AppCompatImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Manager.instance().initialize(this)
        initView()
    }

    fun initView() {
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        val textTitle = findViewById<TextView>(R.id.text_title)
        textTitle.text = title
        toolbarButton = findViewById(R.id.toolbar_button_wifi)
        toolbarButton.setOnClickListener { connect() }
        content1Layout = findViewById(R.id.layout_content1)
        content2Layout = findViewById(R.id.layout_content2)
        statusDefaultLayout = findViewById(R.id.layout_status_default)
        statusDetailLayout = findViewById(R.id.layout_status_detail)
        findViewById<Button>(R.id.button_connect).setOnClickListener { connect() }
        findViewById<Button>(R.id.button_detail).setOnClickListener { openDetail() }
        findViewById<Button>(R.id.button_guide).setOnClickListener { openGuide() }
        findViewById<Button>(R.id.button_notice).setOnClickListener { showNotice() }
        findViewById<Button>(R.id.button_enex).setOnClickListener { openEnex() }

        val filter = IntentFilter()
        filter.addAction(RainpassWifi.ACTION.CONNECTED)
        filter.addAction(RainpassWifi.ACTION.DISCONNECTED)
        registerReceiver(receiver, filter)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == RainpassWifi.ACTION.CONNECTED) {
                refreshUI()
            } else if (action == RainpassWifi.ACTION.DISCONNECTED) {
                refreshUI()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        Manager.instance().finalize(this)
    }

    private fun openDetail() {
        val intent = Intent(this, DetailActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_DETAIL)
    }

    private fun openEnex() {
        val intent = Intent(this, EnexActivity::class.java)
        startActivity(intent)
    }

    private fun openGuide() {
        val intent = Intent(this, GuideActivity::class.java)
        startActivity(intent)
    }

    fun refreshUI() {
        val connected = Manager.instance().isConnected(this)
        if (connected) {
            content1Layout.visibility = View.GONE
            statusDefaultLayout.visibility = View.GONE
            statusDetailLayout.visibility = View.VISIBLE
            content2Layout.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toolbarButton.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.color_wifi_connected, theme))
            } else {
                toolbarButton.setImageDrawable(resources.getDrawable(R.drawable.icon_wifi_green))
            }

            if (!Manager.instance().isNoticeChecked(this)) {
                val handler = Handler()
                handler.postDelayed({
                    showNotice()
                }, 2000)
            }
        } else {
            content1Layout.visibility = View.VISIBLE
            content2Layout.visibility = View.GONE
            statusDefaultLayout.visibility = View.VISIBLE
            statusDetailLayout.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toolbarButton.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.color_wifi_unknown, theme))
            } else {
                toolbarButton.setImageDrawable(resources.getDrawable(R.drawable.icon_wifi))
            }
        }
    }

    fun connect() {
        Toast.makeText(this, getString(R.string.main_toast_loading), Toast.LENGTH_LONG).show()
        Manager.instance().connect(this)
    }

    var isNoticeOpened = false
    private fun showNotice() {
        if (isNoticeOpened) {
            return
        }
        isNoticeOpened = true

        var urlString = Manager.instance().getMarketing(this)
        urlString = "http://rainwifi.rainpass.com:3000/notice" // for test
        if (urlString.isEmpty()) {
            return
        }

        val builder = NoticeDialog.Builder(this, View.OnClickListener {
            isNoticeOpened = false
            Manager.instance().noticeChecked(this)
        })
        builder.setUrl(urlString)
        builder.show()
    }

    fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d("[Main]", "## $message")
        }
    }
}
