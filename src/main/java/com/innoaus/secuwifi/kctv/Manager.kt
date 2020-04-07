package com.innoaus.secuwifi.kctv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.innoaus.rainpass.sdk.RainpassWifi
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Manager {
    companion object {
        private val _instance = Manager()
        fun instance(): Manager {
            return _instance
        }
    }

    fun initialize(context: Context) {
        RainpassWifi.instance().initialize(context, "SECUWIFI", "http://jnhdemo.rainpass.com:9876/api/v1/")
        setCustomInfo(context)
    }

    fun setCustomInfo(context: Context) {
        val prefs = context.getSharedPreferences(PREF_USERINFO, Context.MODE_PRIVATE)
        var userid = prefs.getString(PREF_USERID, "")
        if (userid!!.isEmpty()) {
            userid = UUID.randomUUID().toString().substring(0, 8)
            prefs.edit().putString("userid", userid).apply()
        }

        val customObject = JSONObject()
        customObject.put("userid", userid)
        customObject.put("useremail", "$userid@mail.com")
        RainpassWifi.instance().customInfo(customObject)
    }

    fun finalize(context: Context) {
        RainpassWifi.instance().finalize(context)
    }

    fun isConnected(context: Context): Boolean {
        if (RainpassWifi.instance().isConnected(context)) {
            return true
        }
        return false
    }

    fun connect(context: Context) {
        RainpassWifi.instance().connect(context)
    }

    fun getWifiTicket(context: Context) {
        val ret = RainpassWifi.instance().getWifiTicket(context, "comment", 3600)
        val bool = ret[0] as Boolean
        if (!bool) {
            val message = ret[1] as String
        }
    }

    fun getTicket(): JSONObject? {
        return RainpassWifi.instance().getTicket()
    }

    fun getMarketing(context: Context): String {
        return RainpassWifi.instance().getMarketing(context)
    }

    val PREF_USERINFO = "userInfo"
    val PREF_USERID = "userId"
    val PREF_NOTICE = "notice"
    val PREF_NOTICE_CHECK = "updateDate"
    fun isNoticeChecked(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NOTICE, Context.MODE_PRIVATE)
        val updateDate = prefs.getString(PREF_NOTICE_CHECK, "")
        if (updateDate != null && updateDate.isEmpty()) {
            return false
        }

        val date = Date()
        val now = dateToString(date)
        if (updateDate!! < now) {
            return false
        }
        return true
    }

    fun noticeChecked(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NOTICE, Context.MODE_PRIVATE)
        val date = Date()
        val now = dateToString(date)
        prefs.edit().putString(PREF_NOTICE_CHECK, now).apply()
    }

    private fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.format(date)
    }

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("[Manager]", "## $msg")
        }
    }
}