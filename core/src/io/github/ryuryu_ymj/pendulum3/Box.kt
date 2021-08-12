package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import ktx.box2d.body
import ktx.box2d.box

class Box(asset: AssetManager, world: World, x: Float, y: Float, width: Float, height: Float) :
    Actor() {
    private val tex = asset.get<TextureAtlas>("atlas/play.atlas").findRegion("box")

    init {
        setPosition(x, y)
        setSize(width, height)
        setOrigin(Align.center)
        world.body {
            box(width = width, height = height) {
                restitution = 1f
            }
            position.set(centerX(), centerY())
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(tex, x, y, width, height)
    }
}