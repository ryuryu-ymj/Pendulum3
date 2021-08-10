package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import ktx.actors.then
import kotlin.math.min

class MyCamera(viewportWidth: Float, viewportHeight: Float) :
    OrthographicCamera(viewportWidth, viewportHeight) {
    private val actor = Actor()
    private var target: Body? = null
    private var isChangingTarget = false

    fun act() {
        val dy = viewportHeight / 6
        target?.let { target ->
            if (isChangingTarget) {
                actor.act(min(Gdx.graphics.deltaTime, 1 / 30f))
                position.set(position.x, target.position.y + actor.y + dy, 0f)
            } else {
                position.set(position.x, target.position.y + dy, 0f)
            }
        }
    }

    fun setTarget(value: Body) {
        if (target === value) return
        target = value
        isChangingTarget = true
        actor.setPosition(position.x - value.position.x, position.y - value.position.y)
        actor.clearActions()
        actor.addAction(
            Actions.moveBy(
                value.position.x - position.x,
                value.position.y - position.y,
                0.5f,
            ) then Actions.run {
                isChangingTarget = false
            }
        )
    }
}