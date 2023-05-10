package net.whatscall.freec

import android.app.Application
import net.whatscall.storage.Storage

class WildApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Storage.init(this)
    }

}