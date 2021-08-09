package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.assets.AssetManager
import ktx.app.KtxGame
import ktx.app.KtxScreen

class MyGame : KtxGame<KtxScreen>() {
    val asset = AssetManager()

    override fun create() {
        asset.finishLoading()

        addScreen(PlayScreen(this))
        setScreen<PlayScreen>()
    }
}