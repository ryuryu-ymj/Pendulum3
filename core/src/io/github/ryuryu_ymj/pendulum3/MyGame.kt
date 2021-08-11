package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.load

class MyGame : KtxGame<KtxScreen>() {
    val asset = AssetManager()

    override fun create() {
        asset.load<TextureAtlas>("atlas/play.atlas")
        asset.finishLoading()

        addScreen(PlayScreen(this))
        setScreen<PlayScreen>()
    }
}