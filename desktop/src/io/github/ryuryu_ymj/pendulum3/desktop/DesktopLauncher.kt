package io.github.ryuryu_ymj.pendulum3.desktop

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import io.github.ryuryu_ymj.pendulum3.MyGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            width = 540
            height = 960
        }
        LwjglApplication(MyGame(), config)
    }
}