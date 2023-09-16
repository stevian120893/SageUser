package com.mib.feature_home.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.mib.feature_home.databinding.ActivityWebviewBinding

class WebViewActivity : Activity() {

    var url: String? = ""
    var title: String? = ""
    var from: String? = ""

    private var _binding: ActivityWebviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebviewBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            finish()
        }

        url = intent.getStringExtra(INTENT_EXTRA_WEBVIEW_URL)
        title = intent.getStringExtra(INTENT_EXTRA_WEBVIEW_TITLE)
        from = intent.getStringExtra(INTENT_EXTRA_WEBVIEW_FROM)

        binding.tvTitle.text = title

        if(title!!.toLowerCase().contains("dana")) {
            binding.tvTitle.text = title!!.toUpperCase()
            binding.webview.addJavascriptInterface(JavaScriptInterface(this), "Android")
            binding.webview.settings.javaScriptEnabled = true
        }

        binding.webview.settings.loadsImagesAutomatically = true
        binding.webview.settings.domStorageEnabled = true

//        webview.settings.loadWithOverviewMode = true
        // Init zoom out
        binding.webview.settings.useWideViewPort = true
        // Tiga baris di bawah ini agar laman yang dimuat dapat
        // melakukan zoom.
        binding.webview.settings.setSupportZoom(true)
        binding.webview.settings.builtInZoomControls = true
        binding.webview.settings.displayZoomControls = false

//        webview!!.evaluateJavascript("(function() { return getStringToMyAndroid('" + "Parameter" + "'); })();", ValueCallback<String?> { s ->
//            if(s == "done") {
//
//            }
//        })

        binding.webview.clearCache(true)
        binding.webview.clearHistory()

        // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        binding.webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webview.webViewClient = MyWebViewClient(binding.pbWebview)
        binding.webview.loadUrl(url!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private inner class SSLTolerentWebViewClient : WebViewClient() {
//        private val progressBar: ProgressBar? = null
//        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//            val builder = AlertDialog.Builder(this@WebViewActivity)
//            val alertDialog = builder.create()
//            var message = "SSL Certificate error."
//            when (error.primaryError) {
//                SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
//                SslError.SSL_EXPIRED -> message = "The certificate has expired."
//                SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
//                SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
//            }
//            message += " Do you want to continue anyway?"
//            alertDialog.setTitle("SSL Certificate Error")
//            alertDialog.setMessage(message)
//            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { dialog, which -> // Ignore SSL certificate errors
//                handler.proceed()
//            }
//            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { dialog, which -> handler.cancel() }
//            alertDialog.show()
//        }
//    }

    inner class JavaScriptInterface(val mContext: Context) {
        @JavascriptInterface
        fun appendStatus(status: String, message: String) {
            runOnUiThread {
//                if(status == "success") {
//                    if(from == INTENT_EXTRA_WEBVIEW_FROM_HOME)
//                        EventBus.getDefault().post(EventBusUpdateBindDanaHome(true))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_ORDER_CONFIRMATION_VIEW)
//                        EventBus.getDefault().post(EventBusUpdateBindDanaOrderView(true))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_PAYMENT)
//                        EventBus.getDefault().post(EventBusUpdateAfterPaymentDana(true))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_TOP_UP_HOME)
//                        EventBus.getDefault().post(EventBusUpdateTopUpDana(true))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_TOP_UP_ORDER_CONFIRMATION_VIEW)
//                        EventBus.getDefault().post(EventBusUpdateTopUpDana(true))
//                } else {
//                    if(from == INTENT_EXTRA_WEBVIEW_FROM_HOME)
//                        EventBus.getDefault().post(EventBusUpdateBindDanaHome(false))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_ORDER_CONFIRMATION_VIEW)
//                        EventBus.getDefault().post(EventBusUpdateBindDanaOrderView(false))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_PAYMENT)
//                        EventBus.getDefault().post(EventBusUpdateAfterPaymentDana(false))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_TOP_UP_HOME)
//                        EventBus.getDefault().post(EventBusUpdateTopUpDana(false))
//                    else if(from == INTENT_EXTRA_WEBVIEW_FROM_TOP_UP_ORDER_CONFIRMATION_VIEW)
//                        EventBus.getDefault().post(EventBusUpdateTopUpDana(false))
//                }

                finish()
            }

        }
    }

    private inner class MyWebViewClient(private val progressBar: ProgressBar) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//            view.loadUrl(url)
//            if (Uri.parse(url).getHost().equals(URL)) {
//                // This is your web site, so do not override; let the WebView to load the page
//                return false;
//            }
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
            return false
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)

            // this will ignore the Ssl error and will go forward to your site
            handler.proceed()
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }

        init {
            progressBar.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, WebViewActivity::class.java)
        }
        const val INTENT_EXTRA_WEBVIEW_URL = "webview_url"
        const val INTENT_EXTRA_WEBVIEW_TITLE = "webview_title"
        const val INTENT_EXTRA_WEBVIEW_FROM = "webview_from"
    }
}
