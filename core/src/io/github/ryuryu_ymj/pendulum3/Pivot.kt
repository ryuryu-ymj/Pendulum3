package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import ktx.box2d.body
import ktx.box2d.circle

class Pivot(world: World, centerX: Float, centerY: Float) : Actor() {
    val body: Body

    init {
        setSize(0.2f, 0.2f)
        setOrigin(width / 2, height / 2)
        setPosition(centerX - originX, centerY - originY)
        body = world.body(BodyDef.BodyType.StaticBody) {
            circle(radius = originX) {
                isSensor = true
            }
            position.set(centerX, centerY)
        }
    }
}