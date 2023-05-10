package net.whatscall.storage.database

import android.content.Context
import androidx.room.Room
import net.whatscall.storage.StorageConfig

class StorageBuilder(context: Context) {

    private val builder = Room.databaseBuilder(
        context = context,
        klass = StorageProto::class.java,
        name = StorageConfig.TABLE_NAME
    )

    fun setup(): StorageBuilder {
        builder.fallbackToDestructiveMigration()
        return this
    }

    fun build(): StorageProto {
        return builder.build()
    }

}