package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ray3k.stripe.FreeTypeSkinLoader
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.load
import ktx.scene2d.Scene2DSkin

class MyGame : KtxGame<KtxScreen>() {
    val asset = AssetManager()
    val batch by lazy { SpriteBatch() }

    override fun create() {
        batch

        asset.load<Texture>("img/trail.png")
        asset.load<TextureAtlas>("atlas/play.atlas")
        asset.setLoader(Skin::class.java, FreeTypeSkinLoader(asset.fileHandleResolver))
        asset.load<Skin>("skin/skin.json")
        asset.finishLoading()

        Scene2DSkin.defaultSkin = asset.get("skin/skin.json")

        addScreen(PlayScreen(this))
        setScreen<PlayScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
    }
}