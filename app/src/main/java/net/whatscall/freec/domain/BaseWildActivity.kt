package net.whatscall.freec.domain

import android.net.Uri
import android.webkit.ValueCallback
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import net.whatscall.freec.R

abstract class BaseWildActivity : AppCompatActivity() {

    private var profit: ValueCallback<Array<Uri>>? = null
    private var onBackPress: (() -> Unit)? = null

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPress?.invoke()
        }
    }
    private val ward = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        profit?.onReceiveValue(it.toTypedArray())
    }

    protected open fun eject(profit: ValueCallback<Array<Uri>>?) {
        this.profit = profit
        var mime = getString(R.string.img1)
        mime += getString(R.string.img2)
        mime += getString(R.string.img3)
        ward.launch(mime)
    }

    protected fun setOnBackPress(onBackPress: (() -> Unit)? = null) {
        this.onBackPress = onBackPress
        onBackPressedDispatcher.addCallback(this, backCallback)
    }

    protected var onBackPressEnabled: Boolean
        get() = backCallback.isEnabled
        set(value) { backCallback.isEnabled = value }
}