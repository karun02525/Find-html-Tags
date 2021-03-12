
package io.chingari

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CustomWebChromeClient.ProgressListener {

    val url="http://192.168.43.205:3000/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadWeb()
    }

    private fun loadWeb() {
        progressBar.max = 100
        webView.webChromeClient = CustomWebChromeClient(this)
        webView.settings.javaScriptEnabled = true
        webView!!.settings.setSupportZoom(true)
        webView!!.settings.allowContentAccess = true
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.displayZoomControls = false

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar!!.visibility = View.VISIBLE
            }


            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

            override fun doUpdateVisitedHistory(view: WebView, url: String, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
                Log.d("TAGS"," @@@@@@@@@@@@ : "+ url)
                if (url.contains("#")) {
                    Toast.makeText(applicationContext, "clicked", Toast.LENGTH_SHORT).show()
                }
                if (url.contains("chingari.io/post/latest")) {
                    finish()
                }

            }
        }

        try {
            webView.loadUrl(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        progressBar.progress = 0
    }


    override fun onUpdateProgress(progressValue: Int) {
        progressBar.progress = progressValue
        if (progressValue == 100) {
            progressBar.visibility = View.INVISIBLE
        }
    }


}


class CustomWebChromeClient(private val mListener: ProgressListener) : WebChromeClient() {
    override fun onProgressChanged(view: WebView, newProgress: Int) {
        mListener.onUpdateProgress(newProgress)
        super.onProgressChanged(view, newProgress)
    }

    interface ProgressListener {
        fun onUpdateProgress(progressValue: Int)
    }
}