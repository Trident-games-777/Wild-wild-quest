package net.whatscall.photo_viewer.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume

suspend fun Context.getTrace(): String = suspendCancellableCoroutine {
    val client = InstallReferrerClient.newBuilder(this).build()
    client.startConnection(object : InstallReferrerStateListener {
        override fun onInstallReferrerServiceDisconnected() { }
        override fun onInstallReferrerSetupFinished(responseCode: Int) {
            when (responseCode) {
                InstallReferrerClient.InstallReferrerResponse.OK -> {
                    it.resume(client.installReferrer.installReferrer)
                }
                else -> it.resume(null.toString())
            }
        }
    })
}

val Context.appVersionName get(): String? {
    val flags = PackageManager.GET_META_DATA
    val packageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(flags.toLong())
            )
        } else {
            @Suppress("DEPRECATION") packageManager.getPackageInfo(packageName, flags)
        }
    return packageInfo.versionName
}

fun String.swapEncoding(): String {
    val set = StandardCharsets.UTF_8.toString()
    return URLEncoder.encode(this, set)
}