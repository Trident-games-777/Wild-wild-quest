package net.whatscall.gonzo_hit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.*
import net.whatscall.gonzo_hit.models.GonzoObject
import net.whatscall.gonzo_hit.models.GonzoTag
import net.whatscall.gonzo_hit.models.Vector
import kotlin.math.*
import kotlin.random.Random

class GonzosHitView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private val random = Random(System.currentTimeMillis())
    private var isPaused: Boolean = true
    private var screen: Rect = Rect()

    private val circleBitmapRaw = BitmapFactory.decodeResource(context.resources, R.drawable.circle)
    private val platformBitmapRaw = BitmapFactory.decodeResource(context.resources, R.drawable.platform)
    private val objectsBitmapRaw = listOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_brahma),
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_chest),
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_griph),
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_kha),
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_leaf),
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_ra),
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_tera),
        BitmapFactory.decodeResource(context.resources, R.drawable.obj_vishnu)
    )

    private val circle: GonzoObject = GonzoObject(tag = GonzoTag.CIRCLE)
    private val circleMatrix: Matrix = Matrix()
    private var circleVelocity: Vector = Vector(x = 0.5f, y = -0.5f) * CIRCLE_SPEED

    private val platform: GonzoObject = GonzoObject(tag = GonzoTag.SIDE)
    private var platformVelocity: Vector = Vector.zero
    private var platformActive = false

    private val gonzoHitCollider = GonzoObject(tag = GonzoTag.GAME_OVER)
    private val sides: MutableList<GonzoObject> = MutableList(3) { GonzoObject(tag = GonzoTag.SIDE) }
    private val objects: MutableList<GonzoObject> = mutableListOf()
    private var objectsOffset = 0f

    private var prevTouch: Vector = Vector.zero
    private val velocity: Vector get() = circleVelocity + platformVelocity

    private var gonzoHitListener: GonzoHitListener? = null

    fun setGonzoHitListener(gonzoHitListener: GonzoHitListener) {
        this.gonzoHitListener = gonzoHitListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (!isPaused) {
            when(event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    platformActive = platform.rect.contains(event.x.toInt(), event.y.toInt())
                    platformActive
                }
                MotionEvent.ACTION_MOVE -> {
                    if (platformActive) {
                        if (platformVelocity != Vector.zero) {
                            val currTouch = Vector(event.x, 0f)
                            platformVelocity = currTouch - prevTouch
                        }

                        val width = platform.bitmap!!.width
                        platform.position = platform.position.copy(
                            x = (event.x - width / 2f).coerceIn(0f, screen.width() - width.toFloat()),
                        )
                        prevTouch = Vector(event.x, 0f)
                        true
                    } else false
                }
                MotionEvent.ACTION_UP -> {
                    if (platformActive) {
                        platformActive = false
                        platformVelocity = Vector.zero
                        true
                    } else false
                }
                else -> false
            }
        } else false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screen.set(0, 0, w, h)
        recalculate(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            objects.forEach { obj -> canvas.drawGameObject(obj) }
            canvas.drawGameObject(platform)
            canvas.drawBitmap(
                circle.bitmap!!,
                circleMatrix,
                circle.paint
            )
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }

    fun start() {
        objects.clear()
        objects.addAll(List(OBJECT_WIDTH * OBJECT_HEIGHT_TOTAL) { GonzoObject(tag = GonzoTag.OBJECT) })

        if (!screen.isEmpty) {
            circle.position = Vector.negative
            circleVelocity = Vector(x = 0.5f, y = -0.5f) * CIRCLE_SPEED
            platform.position = Vector.negative
            platformVelocity = Vector.zero
            platformActive = false
            objectsOffset = 0f
            recalculate(screen.width(), screen.height(), screen.width(), screen.height())
        }

        isPaused = false
        scope.launch { gonzoLoop() }
    }

    private suspend fun gonzoLoop() {
        if (!isPaused) {
            delay(16)
            update()
            gonzoLoop()
        }
    }

    private fun update() {
        val newPos = circle.position + velocity
        circle.rect.set(
            newPos.x.toInt(),
            newPos.y.toInt(),
            newPos.x.toInt() + circle.bitmap!!.width,
            newPos.y.toInt() + circle.bitmap!!.height
        )

        checkHit()
        circle.position += velocity
        circleMatrix.setTranslate(circle.position.x, circle.position.y)

        var hasHit = false
        objectsOffset += OBJECT_SPEED
        objects.forEach { obj ->
            if (!hasHit) {
                obj.position = obj.position.copy(
                    y = obj.position.y + OBJECT_SPEED
                )
                if (Rect.intersects(gonzoHitCollider.rect, obj.rect)) {
                    hasHit = true
                    gonzoHitOver()
                }
            }
        }
        invalidate()
    }

    private fun recalculate(w: Int, h: Int, oldw: Int, oldh: Int) {
        // Set platform
        val platformWidth = w / 2.5f
        val platformHeight = (platformWidth / 4.275f).toInt()
        platform.bitmap = Bitmap.createScaledBitmap(
            platformBitmapRaw,
            platformWidth.toInt(),
            platformHeight,
            false
        )
        platform.position = Vector(
            x =
            if (platform.position == Vector.negative) w / 2f - platformWidth / 2f
            else platform.position.x,
            y = h - platformHeight.toFloat()
        )

        // Set circle
        val circleSize = (min(w, h) * 0.1f).toInt()
        circle.bitmap = Bitmap.createScaledBitmap(
            circleBitmapRaw,
            circleSize,
            circleSize,
            false
        )
        circle.position = if (circle.position == Vector.negative) {
            Vector(
                x = w / 2f,
                y = platform.rect.top - circle.bitmap!!.height.toFloat()
            )
        } else {
            Vector(
                x = w / (oldw / circle.position.x),
                y = h / (oldh / circle.position.y),
            )
        }

        // Set sides
        sides[0].rect.set(-SIDE_OFFSET, -SIDE_OFFSET, 0, h + SIDE_OFFSET) // left
        sides[1].rect.set(-SIDE_OFFSET, -SIDE_OFFSET, w + SIDE_OFFSET, 0) // top
        sides[2].rect.set(w, -SIDE_OFFSET, w + SIDE_OFFSET, h + SIDE_OFFSET) // right
        gonzoHitCollider.rect.set(-SIDE_OFFSET, h, w + SIDE_OFFSET, h + SIDE_OFFSET) // bottom

        // Set objects
        val objSize = w / OBJECT_WIDTH
        if (objectsOffset == 0f) objectsOffset = objSize * OBJECT_HEIGHT_SHOWN.toFloat()
        objects.forEachIndexed { i, obj ->
            obj.bitmap = Bitmap.createScaledBitmap(
                objectsBitmapRaw.random(random),
                objSize,
                objSize,
                false
            )
            obj.position = Vector(
                x = i % OBJECT_WIDTH * objSize.toFloat(),
                y = (i / OBJECT_WIDTH + 1) * -objSize + objectsOffset
            )
        }
    }

    private fun checkHit() {
        var hasHit = circleIntersect(gonzoHitCollider)
        if (hasHit) {
            gonzoHitOver()
            return
        }

        hasHit = circleIntersect(platform)
        if (hasHit) return

        sides.forEach { side ->
            if (hasHit.not()) {
                hasHit = circleIntersect(side)
            }
        }
        if (hasHit) return

        var index = -1
        objects.forEachIndexed { i, obj ->
            if (hasHit.not()) {
                hasHit = circleIntersect(obj)
                if (hasHit && index == -1) index = i
            }
        }
        if (index != -1) {
            objects.removeAt(index)
            if (objects.isEmpty()) {
                isPaused = true
                gonzoHitListener?.onWin()
            }
        }
    }

    private fun circleIntersect(gonzoObject: GonzoObject): Boolean {
        var hasHit = false
        if (Rect.intersects(circle.rect, gonzoObject.rect)) {
            hasHit = true

            val newVelocity = circleVelocity.rotate(90f)
            var pos = circle.position + newVelocity
            circle.rect.set(
                pos.x.toInt(),
                pos.y.toInt(),
                pos.x.toInt() + circle.bitmap!!.width,
                pos.y.toInt() + circle.bitmap!!.height
            )

            if (Rect.intersects(circle.rect, gonzoObject.rect)) {
                pos = circle.position + circleVelocity.rotate(-90f)
                circle.rect.set(
                    pos.x.toInt(),
                    pos.y.toInt(),
                    pos.x.toInt() + circle.bitmap!!.width,
                    pos.y.toInt() + circle.bitmap!!.height
                )
                if (Rect.intersects(circle.rect, gonzoObject.rect)) {
                    circle.position = circle.position.copy(
                        y = circle.position.y - velocity.y
                    )
                    circleIntersect(gonzoObject)
                } else {
                    circleVelocity = circleVelocity.rotate(-90f)
                }
            } else {
                circleVelocity = newVelocity
            }
        }
        return hasHit
    }

    private fun gonzoHitOver() {
        isPaused = true
        gonzoHitListener?.onLoose()
    }

    private fun Canvas.drawGameObject(gonzoObject: GonzoObject) {
        drawBitmap(
            gonzoObject.bitmap!!,
            gonzoObject.position.x,
            gonzoObject.position.y,
            gonzoObject.paint
        )
    }

    companion object {
        private const val CIRCLE_SPEED = 20
        private const val SIDE_OFFSET = 100
        private const val OBJECT_WIDTH = 5
        private const val OBJECT_HEIGHT_TOTAL = 8
        private const val OBJECT_HEIGHT_SHOWN = 3
        private const val OBJECT_SPEED = 0.3f
    }
}