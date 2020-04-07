package com.innoaus.secuwifi.kctv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton

class GuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        findViewById<TextView>(R.id.text_title).text = "사용방법"
        findViewById<AppCompatImageButton>(R.id.toolbar_button_back).setOnClickListener { finish() }
    }
}
