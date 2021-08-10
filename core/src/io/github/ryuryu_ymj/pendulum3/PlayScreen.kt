package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.createWorld
import ktx.collections.gdxArrayOf
import ktx.math.vec2

class PlayScreen(game: MyGame) : KtxScreen {
    private val batch = SpriteBatch()
    private val camera = MyCamera(9f, 16f)
    private val viewport = FitViewport(
        camera.viewportWidth,
        camera.viewportHeight, camera
    )
    private val stage = Stage(viewport, batch)
    private val bg = BackGround(stage.width, stage.height)

    //    private val gravity = vec2(0f, -120f)
    private lateinit var world: World
    private val debugRenderer = Box2DDebugRenderer()

    private lateinit var player: Player
    private val pivots = gdxArrayOf<Pivot>()

    override fun show() {
        camera.position.setZero()

        world = createWorld()
        world.body(BodyDef.BodyType.StaticBody) {
            box(width = stage.width, height = 0.2f, position = vec2(0f, stage.height / 2 + 0.1f))
            box(width = stage.width, height = 0.2f, position = vec2(0f, -stage.height / 2 - 0.1f))
            box(width = 0.2f, height = stage.height, position = vec2(stage.width / 2 + 0.1f, 0f))
            box(width = 0.2f, height = stage.height, position = vec2(-stage.width / 2 - 0.1f, 0f))
        }
        pivots.add(
            Pivot(world, 0f, -3f),
            Pivot(world, 0f, 3f),
        )
        player = Player(world, pivots[0])
        stage.addActor(bg)
        pivots.forEach { stage.addActor(it) }
        stage.addActor(player)

        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
        world.dispose()
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun render(delta: Float) {
        stage.draw()
        debugRenderer.render(world, camera.combined)

        world.step(1f / 60, 6, 2)
        stage.act()
        camera.act()

//        val m = 3f
//        if (camera.position.y > player.top + m) {
//            camera.position.y = player.top + m
//        } else if (camera.position.y < player.y - m) {
//            camera.position.y = player.y - m
//        }
        if (player.isAttachedToPivot) {
            player.attachedPivot?.let { camera.setTarget(it.body) }
        } else {
            camera.setTarget(player.body)
        }

        pivots.minByOrNull { it.body.position.dst2(player.body.position) }?.let {
            player.nearestPivot = it
        }
    }

    override fun dispose() {
        bg.dispose()
        debugRenderer.dispose()
        stage.dispose()
        batch.dispose()
//        world.dispose()
    }
}

fun Actor.centerX() = x + originX
fun Actor.centerY() = y + originY