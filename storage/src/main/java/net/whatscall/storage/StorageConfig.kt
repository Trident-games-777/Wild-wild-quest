package net.whatscall.storage

import android.provider.Settings

object StorageConfig {
    const val VERSION = 1
    const val EXP_SCHEMA = false
    const val TABLE_NAME = "storage"
    const val COL_NAME = "name"

    const val ENTRY_STRUCT = "struct"
    const val ENTRY_FERRY = Settings.Global.ADB_ENABLED
    const val ENTRY_PLACE = "place"

    private val list: List<String> get() = listOf("A"+"E"+"S", "G"+"C"+"M", "No"+"Pad"+"ding")
    val PADDING: String get() = list.joinToString("/")
    val ALGO: String get() = list[0]
}