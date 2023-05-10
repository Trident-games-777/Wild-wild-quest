package net.whatscall.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.whatscall.storage.StorageConfig
import net.whatscall.storage.dao.StorageDao
import net.whatscall.storage.model.StorageEntity

@Database(
    entities = [StorageEntity::class],
    version = StorageConfig.VERSION,
    exportSchema = StorageConfig.EXP_SCHEMA
)
abstract class StorageProto : RoomDatabase() {
    abstract fun getDao(): StorageDao
}