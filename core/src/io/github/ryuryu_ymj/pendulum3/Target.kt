package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align

class Target(asset: AssetManager) : Actor() {
    private val tex = asset.get<TextureAtlas>("atlas/play.atlas").findRegion("target")

    init {
        setSize(0.4f, 0.4f)
        setOrigin(Align.center)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(tex, x, y, width, height)
    }
}