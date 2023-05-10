package net.whatscall.storage.repository

import net.whatscall.storage.dao.StorageDao
import net.whatscall.storage.model.StorageEntity

class StorageRepository(
    private val dao: StorageDao
) {

    suspend inline fun <reified T> get(name: String): T? {
        return when(T::class) {
            String::class -> getEntity(name = name)?.string
            Boolean::class -> getEntity(name = name)?.boolean
            Float::class -> getEntity(name = name)?.float
            Int::class -> getEntity(name = name)?.int
            Double::class -> getEntity(name = name)?.double
            else -> throw IllegalStateException("Unsupported type ${T::class}")
        } as? T
    }

    suspend inline fun <reified T> get(name: String, default: T): T {
        return when(T::class) {
            String::class -> getEntity(name = name)?.string
            Boolean::class -> getEntity(name = name)?.boolean
            Float::class -> getEntity(name = name)?.float
            Int::class -> getEntity(name = name)?.int
            Double::class -> getEntity(name = name)?.double
            else -> throw IllegalStateException("Unsupported type ${T::class}")
        } as? T ?: default
    }

    suspend inline fun <reified T> set(name: String, value: T) {
        when(T::class) {
            String::class -> setEntity(StorageEntity(name = name, string = value as String))
            Boolean::class -> setEntity(StorageEntity(name = name, boolean = value as Boolean))
            Float::class -> setEntity(StorageEntity(name = name, float = value as Float))
            Int::class -> setEntity(StorageEntity(name = name, int = value as Int))
            Double::class -> setEntity(StorageEntity(name = name, double = value as Double))
            else -> throw IllegalStateException("Unsupported type ${T::class}")
        }
    }

    suspend fun getEntity(name: String): StorageEntity? {
        return dao.getByName(name)
    }

    suspend fun setEntity(value: StorageEntity) {
        dao.insert(value)
    }
}