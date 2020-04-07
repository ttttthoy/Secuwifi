package com.innoaus.secuwifi.kctv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_enex.*
import java.util.*
import kotlin.random.Random

class EnexActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enex)
        title = "출퇴근 관리하기"

        button_enter.setOnClickListener { Toast.makeText(this, "출근완료", Toast.LENGTH_SHORT).show() }
        button_exit.setOnClickListener { Toast.makeText(this, "퇴근완료", Toast.LENGTH_SHORT).show() }

        val date = Date()
        val random = Random(date.time)
        val num = (random.nextInt() % 3) + 1
        val name = "background" + num
        var resId = resources.getIdentifier(name, "drawable", packageName)
        if (resId == 0) {
            resId = resources.getIdentifier("background1", "drawable", packageName)
        }
        val layout = findViewById<RelativeLayout>(R.id.rootLayout)
        layout.background = resources.getDrawable(resId)
    }
}
