package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import ktx.box2d.body
import ktx.box2d.circle

class Coin(asset: AssetManager, world: World, centerX: Float, centerY: Float) : Actor() {
    private val tex = asset.get<TextureAtlas>("atlas/play.atlas").findRegion("coin")
    val body: Body
    var isObtained = false; private set

    init {
        setSize(0.2f, 0.2f)
        setOrigin(Align.center)
        setPosition(centerX, centerY, Align.center)
        body = world.body(BodyDef.BodyType.StaticBody) {
            circle(radius = width / 2) {
                isSensor = true
            }
            position.set(centerX, centerY)
            userData = this@Coin
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(tex, x, y, width, height)
    }

    fun obtain() {
        isObtained = true
        obtainCoin()
        remove()
    }
}