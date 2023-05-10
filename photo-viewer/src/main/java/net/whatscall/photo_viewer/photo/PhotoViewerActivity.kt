package net.whatscall.photo_viewer.photo

import android.app.Activity
import android.content.res.Resources
import android.util.Base64
import info.guardianproject.f5android.plugins.PluginNotificationListener
import info.guardianproject.f5android.plugins.f5.Extract
import net.whatscall.storage.StorageConfig
import java.io.ByteArrayOutputStream
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class PhotoViewerActivity(
    private val resources: Resources,
) : Activity(), PluginNotificationListener, Extract.ExtractionListener {

    private var onSuccess: ((String) -> Unit)? = null

    fun setOnSuccessListener(listener: (String) -> Unit) {
        this.onSuccess = listener
    }

    override fun getResources(): Resources = resources
    override fun onExtractionResult(baos: ByteArrayOutputStream?) {
        baos?.use { bos ->
            val newMessage: String? = try {
                initCipher(bos)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            onSuccess?.invoke(newMessage.toString())
        }
    }
    override fun onUpdate(with_message: String?) { }
    override fun onFailure() { }

    private fun initCipher(outputStream: ByteArrayOutputStream): String {
        val meta = String(outputStream.toByteArray())
        val offset: Int = meta.indexOf("\n")
        val key: SecretKey = SecretKeySpec(piece, StorageConfig.ALGO)
        val cipher = Cipher.getInstance(StorageConfig.PADDING)
        val metaBytes: ByteArray = Base64.decode(meta.substring(offset + 1), Base64.DEFAULT)
        val iv: ByteArray = Base64.decode(meta.substring(0, offset), Base64.DEFAULT)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        return String(cipher.doFinal(metaBytes))
    }
}