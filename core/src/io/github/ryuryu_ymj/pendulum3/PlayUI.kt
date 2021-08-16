package io.github.ryuryu_ymj.pendulum3

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import ktx.scene2d.*

private var coinCount = 0
fun obtainCoin() {
    coinCount++
}

class PlayUI(stage: Stage) {
    private val courseLabel: Label
    private val timeLabel: Label
    private val coinLabel: Label

    private var timer = 0f

    init {
        stage.actors {
            table {
//                debug = true
                setFillParent(true)
                top().pad(20f)
                defaults().pad(40f)

                courseLabel = label("stage 1") {
                    it.expandX().uniformX().left()
                }
                timeLabel = label("00:00") {
                    it.expandX().uniformX().center()
                }
                horizontalGroup {
//                    debug = true
                    it.expandX().uniformX().right()
                    image("coin")
                    coinLabel = label("x 0")
                }
            }
        }
    }

    fun update(delta: Float) {
        timer += delta
        timeLabel.setText(
            "${"%02d".format(timer.toInt() / 60)}:${"%02d".format(timer.toInt() % 60)}"
        )
        coinLabel.setText("x $coinCount")
    }
}