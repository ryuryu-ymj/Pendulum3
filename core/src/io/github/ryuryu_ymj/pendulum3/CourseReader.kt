package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.GdxRuntimeException
import ktx.box2d.body
import ktx.box2d.box
import ktx.collections.GdxArray
import ktx.math.vec2

class CourseReader {
    var height = 0f

    fun readCourse(
        index: Int,
        asset: AssetManager,
        world: World,
        pivots: GdxArray<Pivot>,
        boxes: GdxArray<Box>,
        coins: GdxArray<Coin>,
        stageWidth: Float
    ) {
        val text: String
        try {
            val file = Gdx.files.internal("course/${"%02d".format(index)}")
            text = file.readString()
        } catch (e: GdxRuntimeException) {
            Gdx.app.error(
                "my-error", "cannot read course file at course/${"%02d".format(index)}", e
            )
            return
        }
        var state = 0
        for (line in text.lines()) {
            if (line[0] == '#') {
                state++
                continue
            }
            when (state) {
                1 -> { // course config
                    height = line.toFloat()
                    val b = 0.2f
                    world.body {
                        box(width = stageWidth, height = b, position = vec2(0f, -b / 2)) {
                            restitution = 1f
                        }
                        box(width = stageWidth, height = b, position = vec2(0f, height + b / 2)) {
                            restitution = 1f
                        }
                        box(
                            width = b,
                            height = height,
                            position = vec2(-stageWidth / 2 - b / 2, height / 2)
                        ) {
                            restitution = 1f
                        }
                        box(
                            width = b,
                            height = height,
                            position = vec2(stageWidth / 2 + b / 2, height / 2)
                        ) {
                            restitution = 1f
                        }
                    }
                }
                2 -> { // pivot
                    val cells = line.split(' ')
                    val x = cells[0].toFloat()
                    val y = cells[1].toFloat()
                    pivots.add(Pivot(asset, world, x, y))
                }
                3 -> { // box
                    val cells = line.split(' ')
                    val x = cells[0].toFloat()
                    val y = cells[1].toFloat()
                    val w = cells[2].toFloat()
                    val h = cells[3].toFloat()
                    boxes.add(Box(asset, world, x, y, w, h))
                }
                4 -> { // coin
                    val cells = line.split(' ')
                    val x = cells[0].toFloat()
                    val y = cells[1].toFloat()
                    coins.add(Coin(asset, world, x, y))
                }
            }
        }
    }
}