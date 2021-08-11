package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint
import com.badlogic.gdx.scenes.scene2d.Actor
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.distanceJointWith
import kotlin.math.max

class Player(asset: AssetManager, private val world: World, startPivot: Pivot) : Actor() {
    val body: Body
    var nearestPivot: Pivot = startPivot
    var attachedPivot: Pivot? = null; private set
    private var joint: DistanceJoint? = null
    val isAttachedToPivot; get() = joint != null
    private val speed = 5f

    private val effect = ParticleEffect()

    init {
        setSize(0.4f, 0.4f)
        setPosition(startPivot.x + startPivot.originX + 3f, startPivot.y + startPivot.originY)
        body = world.body(BodyDef.BodyType.DynamicBody) {
            circle(radius = width / 2) {
                density = 1f
                friction = 0f
                restitution = 1f
            }
            fixedRotation = true
            position.set(x + width / 2, y + height / 2)
        }
        body.applyLinearImpulse(0f, speed * body.mass, body.worldCenter.x, body.worldCenter.y, true)
        attachJoint()

        effect.load(Gdx.files.internal("particle/trail.p"), asset.get<TextureAtlas>("atlas/play.atlas"))
        effect.setPosition(centerX(), centerY())
//        effect.setPosition(1f, 1f)
        effect.start()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        effect.draw(batch)
    }

    override fun act(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (nearestPivot !== attachedPivot) {
                attachJoint()
            } else {
                detachJoint()
            }
        }

        val l = body.linearVelocity.len()
        val k = (l - speed) * 10
//        println("speed:$l, force:(${-k * body.linearVelocity.x / l * body.mass},${-k * body.linearVelocity.y / l * body.mass})")
//        println(l)
        body.applyForce(
            -k * body.linearVelocity.x / l * body.mass,
            -k * body.linearVelocity.y / l * body.mass,
            body.worldCenter.x,
            body.worldCenter.y,
            true
        )
        setPosition(body.position.x - originX, body.position.y - originY)
        effect.setPosition(centerX(), centerY())
        effect.update(delta)
    }

    private fun attachJoint() {
        joint?.let { world.destroyJoint(it) }
        joint = body.distanceJointWith(nearestPivot.body) {
            length = max(body.position.dst(nearestPivot.body.position), 0.5f)
        }
        attachedPivot = nearestPivot
    }

    private fun detachJoint() {
        joint?.let { world.destroyJoint(it) }
        joint = null
        attachedPivot = null
    }
}