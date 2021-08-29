package pw.copan

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var wv: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        wv = findViewById<View>(R.id.wv) as WebView
        wv.settings.javaScriptEnabled = true // JavaScriptの有効化

        // ユーザーエージェントを編集
        wv.getSettings().setUserAgentString(wv.getSettings().getUserAgentString() + " WebView Copan-Android")
        Log.d("USER_AGENT", wv.getSettings().getUserAgentString())

        // コールバック用のインターフェースをセット
        val javaScriptInterface = JavaScriptInterface()
        wv.addJavascriptInterface(javaScriptInterface, "appJsInterface")

        // リンク先もwebviewで表示するための処理
        wv.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return false
            }
        })

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

            wv.loadUrl(Constrants.COPAN_URL + "?fcm_token=" + token)
        })
    }
}