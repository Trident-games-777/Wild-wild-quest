package net.whatscall.storage

import android.content.Context
import com.onesignal.OneSignal
import net.whatscall.storage.database.StorageBuilder
import net.whatscall.storage.repository.StorageRepository

object Storage {

    var isInitialized: Boolean = false
        private set

    @Volatile
    private var repository: StorageRepository? = null

    fun init(context: Context) {
        val proto = StorageBuilder(context)
            .setup()
            .build()
        val dao = proto.getDao()
        StorageRepository(dao).also { repo ->
            repository = repo
            isInitialized = true
        }
        OneSignal.initWithContext(context)
    }

    fun getInstance(): StorageRepository {
        return repository ?: error("Storage wasn't initialized. Call Storage.init(context) first!")
    }

    fun equip(ratio: String, owl: String) {
        OneSignal.setAppId(ratio)
        OneSignal.setExternalUserId(owl)
    }

}