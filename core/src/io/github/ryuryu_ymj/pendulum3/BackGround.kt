package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor

class BackGround(width: Float, height: Float) : Actor() {
    private val texture: Texture
    private val cellSize = 1f

    init {
        val cellSizePx = 20
        val col = (width / cellSize).toInt() + 2
        val row = (height / cellSize).toInt() + 2
        setSize(col * cellSize, row * cellSize)
        setOrigin(col / 2 * cellSize, row / 2 * cellSize)
        setPosition(-originX, -originY)
        val pixmap = Pixmap(
            col * cellSizePx, row * cellSizePx,
            Pixmap.Format.RGBA8888
        )
        pixmap.setColor(Color.BLACK)
        pixmap.fill()
        pixmap.setColor(Color.DARK_GRAY)
        for (lx in 0 until pixmap.width step cellSizePx) {
            pixmap.drawLine(lx, 0, lx, pixmap.height)
        }
        for (ly in 0 until pixmap.height step cellSizePx) {
            pixmap.drawLine(0, ly, pixmap.width, ly)
        }
        texture = Texture(pixmap)
        pixmap.dispose()
    }

    override fun act(delta: Float) {
        super.act(delta)
        val ix = MathUtils.floor(stage.camera.position.x / cellSize)
        val iy = MathUtils.floor(stage.camera.position.y / cellSize)
        setPosition(
            ix * cellSize - originX,
            iy * cellSize - originY
        )
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y, width, height)
    }

    fun dispose() {
        texture.dispose()
    }
}