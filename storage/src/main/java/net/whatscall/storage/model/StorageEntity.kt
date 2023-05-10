package net.whatscall.storage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.whatscall.storage.StorageConfig

@Entity(tableName = StorageConfig.TABLE_NAME)
data class StorageEntity(
    @PrimaryKey
    @ColumnInfo(name = StorageConfig.COL_NAME)
    var name: String = "",
    var string: String? = null,
    var boolean: Boolean? = null,
    var float: Float? = null,
    var int: Int? = null,
    var double: Double? = null,
)
