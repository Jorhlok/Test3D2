package net.jorhlok.test3d2

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.utils.Array

class Test3D2 : ApplicationAdapter() {

    var environment: Environment? = null
    var cam: Camera? = null
    var camController: FirstPersonControllerCameraController? = null
    var modelBatch: ModelBatch? = null
    var instances = Array<ModelInstance>()
    var floor: Heightmap? = null
    var pointer: Pointer? = null

    override fun create() {
        modelBatch = ModelBatch()
        environment = Environment()
        environment!!.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment!!.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))

//        DefaultShader.defaultCullFace = 0

        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam!!.position.set(8f, 1f, -8f)
        cam!!.lookAt(8f, 1f, 0f)
        cam!!.near = 1/64f
        cam!!.far = 1024f
        cam!!.update()

        camController = FirstPersonControllerCameraController(cam!!)
        Gdx.input.inputProcessor = camController

        val w = 64
        val h = 32
        val arr = FloatArray((w+1)*(h+1))

        for (i in 0 until (w+1)*(h+1))
            arr[i] = Math.random().toFloat()

        floor = Heightmap(w,h,arr)
        instances.add(floor!!.dasinstance)

        pointer = Pointer()
        pointer!!.floor = floor
        instances.add(pointer!!.dasinstance)
    }

    override fun render() {

        val deltatime = Gdx.graphics.deltaTime

        //do stuff
        camController!!.update(deltatime)

        pointer!!.update(deltatime,camController!!.dpad)


        //drawings now
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(0f,0.5f,1f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch!!.begin(cam)
        modelBatch!!.render(instances, environment)
        modelBatch!!.end()
    }

    override fun dispose() {
        modelBatch?.dispose()
        instances.clear()
        floor?.dispose()
    }
}
