package pw.copan

import android.util.Log
import android.webkit.JavascriptInterface

class JavaScriptInterface {
    @JavascriptInterface
    fun startCopan(sessionId: String) {
        Log.d("sessionId : ", sessionId)
    }
}