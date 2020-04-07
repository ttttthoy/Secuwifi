package com.innoaus.secuwifi.kctv

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.ArrayList

class LaunchActivity : AppCompatActivity() {
    private val handler = Handler()
    private var permissionResult: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        checkPermission()
    }

    private val runnableToMain = Runnable {
        if (permissionResult) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, R.string.msg_error_splash2, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        val list = ArrayList<String>()
        val perm = Manifest.permission.ACCESS_COARSE_LOCATION
        if (checkSelf(perm) == PackageManager.PERMISSION_DENIED) {
            // 권한 없음
            list.add(perm)
        }

        if (list.size > 0) {
            val arr = arrayOfNulls<String>(list.size)
            for (i in list.indices) {
                arr[i] = list[i]
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (arr.isNotEmpty())
                    requestPermissions(arr, 8282)
            }
        } else {
            handler.postDelayed(runnableToMain, 800)
        }
    }

    private fun checkSelf(permission: String): Int {
        return ContextCompat.checkSelfPermission(this, permission)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 8282) {
            for (i in permissions.indices) {
                if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                    this.permissionResult = true
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다

                    //msg = getString(R.string.msg_permission_missing)
                    val msg = getString(R.string.msg_error_splash1)
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    this.permissionResult = false
                }
            }

            handler.postDelayed(runnableToMain, 800)
        }
    }
}
