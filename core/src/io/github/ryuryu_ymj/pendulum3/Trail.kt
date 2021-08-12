package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import kotlin.math.hypot

class Trail(asset: AssetManager, initX: Float, initY: Float) {
    private val cx = FloatHistory(50) { initX }
    private val cy = FloatHistory(cx.size) { initY }
    private val nx = FloatHistory(cx.size) { 0f }
    private val ny = FloatHistory(cx.size) { 0f }
    private val texture: Texture = asset.get("img/trail.png")

    fun update(x: Float, y: Float) {
        cx.add(x)
        cy.add(y)
        val vx = cx[1] - cx[0]
        val vy = cy[1] - cy[0]
        val vl = hypot(vx, vy)
        nx.add(-vy / vl)
        ny.add(vx / vl)
    }

    fun draw(batch: Batch) {
        val v = FloatArray(20)
        var w = 0.2f
        val dw = w / (cx.size - 1)
        v[10] = cx[0] + nx[0] * w / 2 // x3
        v[11] = cy[0] + ny[0] * w / 2 // y3
        v[15] = cx[0] - nx[0] * w / 2 // x4
        v[16] = cy[0] - ny[0] * w / 2 // y4
        val c = Color(1f, 1f, 1f, 0.8f)
        val da = c.a / (cx.size - 1)
        v[12] = c.toFloatBits() // c3
        v[17] = c.toFloatBits() // c4
        v[4] = 1f // v1
        v[8] = 1f // u2
        v[9] = 1f // v2
        v[13] = 1f // u3
        for (i in 0..cx.size - 2) {
            v[0] = v[15] // x1
            v[1] = v[16] // y1
            v[5] = v[10] // x2
            v[6] = v[11] // y2
            w -= dw
            v[10] = cx[i + 1] + nx[i + 1] * w / 2 // x3
            v[11] = cy[i + 1] + ny[i + 1] * w / 2 // y3
            v[15] = cx[i + 1] - nx[i + 1] * w / 2 // x4
            v[16] = cy[i + 1] - ny[i + 1] * w / 2 // y4

            v[2] = v[12] // c1
            v[7] = v[12] // c2
            c.a -= da
            v[12] = c.toFloatBits() // c3
            v[17] = c.toFloatBits() // c4

            batch.draw(texture, v, 0, v.size)
        }
    }
}

class FloatHistory(val size: Int, init: (Int) -> Float) {
    private val array = FloatArray(size, init)
    private var head = 0

    operator fun get(index: Int): Float {
        var i = index + head
        if (i >= size) i -= size
        return array[i]
    }

    fun add(value: Float) {
        if (head == 0) head = size - 1
        else head--
        array[head] = value
    }
}