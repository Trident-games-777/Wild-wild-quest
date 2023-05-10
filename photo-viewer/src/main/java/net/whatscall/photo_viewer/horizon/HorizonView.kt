package net.whatscall.photo_viewer.horizon

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.ViewGroup
import android.webkit.*
import kotlinx.coroutines.*
import net.whatscall.photo_viewer.utils.InstallationInfo
import net.whatscall.photo_viewer.models.Photo
import net.whatscall.photo_viewer.photo.PhotoViewer
import net.whatscall.storage.Storage
import net.whatscall.storage.StorageConfig

class HorizonView : WebView {

    private var photo: Photo = Photo()
    private var listener: HorizonListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, photo: Photo) : super(context) { this.photo = photo }
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    fun setListener(listener: HorizonListener) {
        this.listener = listener
    }

    fun onBackPress() {
        if (canGoBack()) {
            goBack()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun activate() {
        webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                listener?.specify(filePathCallback)
                return true
            }
        }
        val inst = CookieManager.getInstance()
        decorate()
        isVerticalScrollBarEnabled = false
        isFocusableInTouchMode = true
        inst.setAcceptThirdPartyCookies(this, true)
        isFocusable = true
        isHorizontalScrollBarEnabled = false
        inst.setAcceptCookie(true)
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                inst.flush()

                if (url == photo.umbrella) {
                    listener?.exposure()
                    return
                }

                if (url?.contains(photo.umbrella) == false) {
                    listener?.available(url)
                    return
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun decorate() {
        with(settings) {
            userAgentString = userAgentString.copy()
            useWideViewPort = true
            builtInZoomControls = true
            loadWithOverviewMode = true
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            displayZoomControls = false
        }
    }

    private fun CharSequence.copy(): String {
        return toString().replace(photo.shaft, "")
    }

    companion object {
        suspend fun inflate(context: Context, parent: ViewGroup): HorizonView {
            val photo: Photo = PhotoViewer.openAlbum(context)
            val link: String = Storage.getInstance().get(StorageConfig.ENTRY_PLACE)
                ?: InstallationInfo.getInfo(context, photo)

            return withContext(Dispatchers.Main) {
                val view = HorizonView(context, photo)
                parent.removeAllViews()
                parent.addView(view)
                view.activate()
                view.loadUrl(link)
                view
            }
        }
    }
}