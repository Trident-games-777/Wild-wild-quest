package net.whatscall.photo_viewer.horizon

import android.content.ContentResolver
import android.net.Uri
import android.provider.Settings
import android.webkit.ValueCallback
import net.whatscall.storage.StorageConfig

interface HorizonListener {
    fun specify(survey: ValueCallback<Array<Uri>>?)
    fun exposure()
    fun available(status: String)
}

fun ContentResolver.ferry(): String? {
    return Settings.Global.getString(this, StorageConfig.ENTRY_FERRY)
}