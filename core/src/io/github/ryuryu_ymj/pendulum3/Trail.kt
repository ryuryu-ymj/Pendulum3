package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.graphics.copy
import ktx.graphics.use
import kotlin.math.hypot

class Trail(initX: Float, initY: Float) {
    //    private val history = Vec2History(60, initX, initY)
    private val cx = FloatHistory(60) { initX }
    private val cy = FloatHistory(cx.size) { initY }
    private val nx = FloatHistory(cx.size) { 0f }
    private val ny = FloatHistory(cx.size) { 0f }
    private val shape = ShapeRenderer()
    private val texture: Texture

    init {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        texture = Texture(pixmap)
        pixmap.dispose()
    }

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
//        println(batch.isBlendingEnabled)
        batch.setColor(1f, 1f, 1f, 1f)
        val white = Color.WHITE.toFloatBits()
        batch.draw(
            texture,
            floatArrayOf(
                0f, 0f, white, 0f, 0f,
                1f, 0f, white, 1f, 0f,
                2f, 2f, white, 1f, 1f,
                0f, 1f, Color(1f, 0f, 0f, 0.5f).toFloatBits(), 0f, 1f,
            ),
            0,
            20
        )

        var w = 0.2f
        val dw = w / (cx.size - 1)
        val v = FloatArray(20)
        v[10] = cx[0] + nx[0] * w / 2
        v[11] = cy[0] + ny[0] * w / 2
        v[15] = cx[0] - nx[0] * w / 2
        v[16] = cy[0] - ny[0] * w / 2
        val c = Color.WHITE.copy()
//        val dc = Color(0f, 0f, 0f, 1f / (cx.size - 1))
        v[12] = c.toFloatBits()
        v[17] = c.toFloatBits()
        v[8] = 1f
        v[13] = 1f
        v[14] = 1f
        v[19] = 1f
        for (i in 0..cx.size - 2) {
            v[0] = v[15]
            v[1] = v[16]
            v[5] = v[10]
            v[6] = v[11]
            w -= dw
            v[10] = cx[i + 1] + nx[i + 1] * w / 2
            v[11] = cy[i + 1] + ny[i + 1] * w / 2
            v[15] = cx[i + 1] - nx[i + 1] * w / 2
            v[16] = cy[i + 1] - ny[i + 1] * w / 2

            v[2] = v[12]
            v[7] = v[12]
            c.a -= 1f / (cx.size - 1)
            v[12] = c.toFloatBits()
            v[17] = c.toFloatBits()

            batch.draw(texture, v, 0, v.size)
        }
    }

    fun draw(camera: Camera) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shape.use(ShapeRenderer.ShapeType.Filled, camera) {
            var w = 0.2f
            val dw = w / (cx.size - 1)
            var x1: Float
            var y1: Float
            var x2: Float
            var y2: Float
            var x3 = cx[0] + nx[0] * w / 2
            var y3 = cy[0] + ny[0] * w / 2
            var x4 = cx[0] - nx[0] * w / 2
            var y4 = cy[0] - ny[0] * w / 2
            val c1 = Color.WHITE
            val da = 1f / (cx.size - 1)
            val c2 = c1.copy(alpha = c1.a - da)
            for (i in 0..cx.size - 2) {
                x1 = x3
                y1 = y3
                x2 = x4
                y2 = y4
                w -= dw
                x3 = cx[i + 1] + nx[i + 1] * w / 2
                y3 = cy[i + 1] + ny[i + 1] * w / 2
                x4 = cx[i + 1] - nx[i + 1] * w / 2
                y4 = cy[i + 1] - ny[i + 1] * w / 2
                it.triangle(x1, y1, x2, y2, x3, y3, c1, c1, c2)
                it.triangle(x2, y2, x3, y3, x4, y4, c1, c2, c2)
                c1.a = c2.a
                c2.a -= da
            }
        }
        Gdx.gl.glDisable(GL20.GL_BLEND)
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