package net.jorhlok.test3d2.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import net.jorhlok.test3d2.Test3D2

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = 1280
        config.height = 720
        LwjglApplication(Test3D2(), config)
    }
}
