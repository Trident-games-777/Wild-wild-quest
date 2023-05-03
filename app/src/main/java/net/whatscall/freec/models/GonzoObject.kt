package net.whatscall.freec.models

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Rect

data class GonzoObject(
    var bitmap: Bitmap? = null,
    val rect: Rect = Rect(),
    val paint: Paint = Paint().apply { isAntiAlias = true },
    val tag: GonzoTag = GonzoTag.SIDE,
) {
    var position: Vector = Vector.negative
        set(value) {
            field = value
            rect.set(
                value.x.toInt(),
                value.y.toInt(),
                value.x.toInt() + bitmap!!.width,
                value.y.toInt() + bitmap!!.height
            )
        }
}