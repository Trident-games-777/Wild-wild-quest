package net.whatscall.freec.models

import android.graphics.Rect
import kotlin.math.*

data class Vector(
    val x: Float,
    val y: Float
) {

    constructor(x: Int, y: Int) : this(x = x.toFloat(), y = y.toFloat())

    val magnitude: Float
        get() = sqrt(x * x + y * y)

    val sqrMagnitude: Float
        get() = x * x + y * y

    val normalized: Vector
        get() = magnitude.let { mag ->
            if (mag > kEpsilon) this / mag
            else zero
        }

    val max: Float
        get() = max(abs(x), abs(y))

    operator fun plus(p: Vector): Vector {
        return Vector(
            x = x + p.x,
            y = y + p.y
        )
    }
    operator fun minus(p: Vector): Vector {
        return Vector(
            x = x - p.x,
            y = y - p.y
        )
    }
    operator fun times(p: Vector): Vector {
        return Vector(
            x = x * p.x,
            y = y * p.y
        )
    }
    operator fun times(f: Float): Vector {
        return Vector(
            x = x * f,
            y = y * f
        )
    }
    operator fun times(i: Int): Vector {
        return Vector(
            x = x * i,
            y = y * i
        )
    }
    operator fun div(p: Vector): Vector {
        return Vector(
            x = x / p.x,
            y = y / p.y
        )
    }
    operator fun div(f: Float): Vector {
        return Vector(
            x = x / f,
            y = y / f
        )
    }
    operator fun div(i: Int): Vector {
        return Vector(
            x = x / i,
            y = y / i
        )
    }

    fun rotate(delta: Float): Vector {
        val del = (delta * (PI * 2) / 360).toFloat()
        return Vector(
            x = x * cos(del) - y * sin(del),
            y = x * sin(del) + y * cos(del)
        )
    }

    fun contains(v: Vector): Boolean {
        return v.x in 0f..x && v.y in 0f..y
    }

    companion object {
        const val kEpsilon = 0.00001f
        const val kEpsilonNormalSqrt = 1e-15f

        val negative: Vector = Vector(x = -1f, y = -1f)
        val zero: Vector = Vector(x = 0f, y = 0f)
        val one: Vector = Vector(x = 1f, y = 1f)
        val up: Vector = Vector(x = 0f, y = 1f)
        val left: Vector = Vector(x = -1f, y = 0f)
        val right: Vector = Vector(x = 1f, y = 0f)
        val down: Vector = Vector(x = 0f, y = -1f)

        fun dot(lhs: Vector, rhs: Vector): Float {
            return lhs.x * rhs.x + lhs.y * rhs.y
        }

        fun angle(from: Vector, to: Vector): Float {
            val denominator = sqrt(from.sqrMagnitude * to.sqrMagnitude)
            if (denominator < kEpsilonNormalSqrt) return 0f

            val dot = (dot(from, to) / denominator).coerceIn(-1f, 1f)
            return acos(dot)
        }
    }
}

fun Rect.contains(vector: Vector): Boolean {
    return contains(vector.x.toInt(), vector.y.toInt())
}