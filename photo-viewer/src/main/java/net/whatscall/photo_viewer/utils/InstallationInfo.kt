package net.whatscall.photo_viewer.utils

import android.content.Context
import android.os.Build
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.whatscall.photo_viewer.models.Photo
import net.whatscall.storage.Storage
import java.util.*

object InstallationInfo {

    suspend fun getInfo(context: Context, user: Photo): String = withContext(Dispatchers.IO) {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val dataMap = mapOf(
            /*appVersionKey */user.scramble to context.appVersionName,
            /*packageNameKey*/user.civilization to context.packageName,
            /*gadIdKey      */user.cart to AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString(),
            /*userAgentKey  */user.coverage to String.format(user.television, Build.VERSION.RELEASE, Locale.getDefault().toString(), Build.MODEL, Build.ID),
            /*referrerKey   */user.represent to context.getTrace(),
            /*timestampKey  */user.laundry to System.currentTimeMillis(),
            /*osVersionKey  */user.stock to Build.VERSION.RELEASE,
        )
        Storage.equip(user.speaker, dataMap[user.cart].toString())
        user.route + gson.toJson(dataMap).swapEncoding()
    }

}
