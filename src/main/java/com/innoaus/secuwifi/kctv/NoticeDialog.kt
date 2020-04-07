package com.innoaus.secuwifi.kctv

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button

class NoticeDialog(context: Context) : Dialog(context) {
    var url: String = ""
    private var closeListener: View.OnClickListener? = null

    constructor(context: Context, listener1: View.OnClickListener) : this(context) {
        this.closeListener = listener1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_notice)
        setCanceledOnTouchOutside(false)
        //window?.setBackgroundDrawable(ColorDrawable())
        window?.setDimAmount(0.4f)
        val closeButton = findViewById<Button>(R.id.dialog_close_button)
        if (closeListener != null) {
            closeButton.setOnClickListener {
                closeListener!!.onClick(it)
                dismiss()
            }
        }

        val webview = findViewById<WebView>(R.id.notice_webview)
        webview.webChromeClient = WebChromeClient()
        webview.webViewClient = WebViewClient()
        val settings = webview.settings
        settings.setSupportMultipleWindows(false)
        settings.javaScriptEnabled = true
        settings.setSupportZoom(true)
        //settings.builtInZoomControls = false
        if (!url.isEmpty()) {
            webview.loadUrl(url)
        } else {
            dismiss()
        }
    }

    class Builder(context: Context, listener1: View.OnClickListener) {
        private val dialog = NoticeDialog(context, listener1)
        fun setUrl(text: String): Builder {
            dialog.url = text
            return this
        }

        fun show(): NoticeDialog {
            dialog.show()
            return dialog
        }
    }
}