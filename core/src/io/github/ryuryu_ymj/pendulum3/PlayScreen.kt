package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.box2d.createWorld
import ktx.collections.gdxArrayOf
import kotlin.math.min

class PlayScreen(private val game: MyGame) : KtxScreen {
    private val camera = OrthographicCamera(9f, 16f)
    private val viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)
    private val stage = Stage(viewport, game.batch)
    private val bg = BackGround(stage.width, stage.height)

    private val uiCamera = OrthographicCamera(2160f, 3840f)
    private val uiViewport = FitViewport(uiCamera.viewportWidth, uiCamera.viewportHeight, uiCamera)
    private val uiStage = Stage(uiViewport, game.batch)
    private val playUI = PlayUI(uiStage)

    private lateinit var world: World
    private val debugRenderer = Box2DDebugRenderer()

    private val course = CourseReader()
    private lateinit var player: Player
    private val target = Target(game.asset)
    private val pivots = gdxArrayOf<Pivot>()
    private val boxes = gdxArrayOf<Box>()
    private val coins = gdxArrayOf<Coin>()

    override fun show() {
        world = createWorld()
        course.readCourse(1, game.asset, world, pivots, boxes, coins, stage.width)
        player = Player(game.asset, world, pivots[0])

        stage.addActor(bg)
        boxes.forEach { stage.addActor(it) }
        pivots.forEach { stage.addActor(it) }
        coins.forEach { stage.addActor(it) }
        stage.addActor(player)
        stage.addActor(target)

        camera.position.set(0f, stage.height / 2, 0f)
        player.nearestPivot.let { target.setPosition(it.centerX(), it.centerY(), Align.center) }

        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
        pivots.clear()
        boxes.clear()
        coins.clear()
        stage.clear()
        world.dispose()
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        uiViewport.update(width, height)
    }

    override fun render(delta: Float) {
        // draw
        stage.draw()
        debugRenderer.render(world, camera.combined)
        uiStage.draw()

        // act
        val d = min(Gdx.graphics.deltaTime, 1 / 30f)
        world.step(d, 6, 2)
        stage.act(d)
        uiStage.act(d)
        playUI.update(d)

        if (camera.position.y > course.height - stage.height / 2) {
            camera.position.y = course.height - stage.height / 2
        }
        if (camera.position.y < player.centerY() &&
            camera.position.y < course.height - stage.height / 2
        ) {
            camera.position.y = player.centerY()
        }

        pivots.minByOrNull { it.body.position.dst2(player.body.position) }?.let {
            player.nearestPivot = it
            target.setPosition(it.centerX(), it.centerY(), Align.center)
        }
    }

    override fun dispose() {
        bg.dispose()
        debugRenderer.dispose()
        stage.dispose()
        uiStage.dispose()
//        batch.dispose()
        if (game.shownScreen === this) world.dispose()
    }
}

fun Actor.centerX() = x + originX
fun Actor.centerY() = y + originY