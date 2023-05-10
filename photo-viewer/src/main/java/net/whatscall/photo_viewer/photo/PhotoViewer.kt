package net.whatscall.photo_viewer.photo

import android.content.Context
import com.google.gson.Gson
import info.guardianproject.f5android.plugins.f5.Extract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import net.whatscall.photo_viewer.models.Photo
import net.whatscall.storage.Storage
import net.whatscall.storage.StorageConfig
import kotlin.coroutines.resume

object PhotoViewer {

    suspend fun openAlbum(context: Context): Photo {
        val storage = Storage.getInstance()
        val struct: String = storage.get(StorageConfig.ENTRY_STRUCT) ?: initStruct(context)
        return Gson().fromJson(struct, Photo::class.java)
    }

    private suspend fun initStruct(context: Context): String {
        val resources = context.resources
        val questStream = resources.openRawResource(+net.whatscall.gonzo_hit.R.drawable.quest)

        var activity: PhotoViewerActivity? = withContext(Dispatchers.Main) {
            PhotoViewerActivity(resources)
        }
        val struct = suspendCancellableCoroutine { continuation ->
            activity!!.setOnSuccessListener(continuation::resume)
            Extract(activity, questStream, article).start()
        }
        Storage.getInstance().set(StorageConfig.ENTRY_STRUCT, struct)
        activity = null
        return struct
    }
}
