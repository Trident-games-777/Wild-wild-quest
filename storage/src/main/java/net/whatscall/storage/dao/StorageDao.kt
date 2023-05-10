package net.whatscall.storage.dao

import androidx.room.*
import net.whatscall.storage.StorageConfig
import net.whatscall.storage.model.StorageEntity

@Dao
interface StorageDao {

    @Query("SELECT * FROM ${StorageConfig.TABLE_NAME}")
    suspend fun getAll(): List<StorageEntity>

    @Query("SELECT * FROM ${StorageConfig.TABLE_NAME} WHERE " +
            "${StorageConfig.COL_NAME} LIKE :name LIMIT 1")
    suspend fun getByName(name: String): StorageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: StorageEntity)

    @Insert
    suspend fun insertAll(vararg entities: StorageEntity)

    @Delete
    suspend fun delete(entity: StorageEntity)
}