package net.jorhlok.test3d2

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.IntIntMap

class FirstPersonControllerCameraController(private val camera: Camera) : InputAdapter() {
//    private val camera: Camera
    private val keys = IntIntMap()
    private val STRAFE_LEFT = Input.Keys.A
    private val STRAFE_RIGHT = Input.Keys.D
    private val FORWARD = Input.Keys.W
    private val BACKWARD = Input.Keys.S
    private val UP = Input.Keys.Q
    private val DOWN = Input.Keys.E
    private var velocity = 5f
    private var degreesPerPixel = 0.5f
    private val tmp = Vector3()
    var dead = 0.15f
    var joyvelocity = 10f
    var joylook = 2f

//    fun FirstPersonCameraController(camera: Camera): ??? {
//        this.camera = camera
//    }

    override fun keyDown(keycode: Int): Boolean {
        keys.put(keycode, keycode)
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        keys.remove(keycode, 0)
        return true
    }

    /** Sets the velocity in units per second for moving forward, backward and strafing left/right.
     * @param velocity the velocity in units per second
     */
    fun setVelocity(velocity: Float) {
        this.velocity = velocity
    }

    /** Sets how many degrees to rotate per pixel the mouse moved.
     * @param degreesPerPixel
     */
    fun setDegreesPerPixel(degreesPerPixel: Float) {
        this.degreesPerPixel = degreesPerPixel
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val deltaX = -Gdx.input.deltaX * degreesPerPixel
        val deltaY = -Gdx.input.deltaY * degreesPerPixel
//        camera.direction.rotate(camera.up, deltaX)
        tmp.set(camera.direction).crs(camera.up).nor()
        camera.direction.rotate(tmp, deltaY)
        camera.rotate(deltaX,0f,1f,0f)
        // camera.up.rotate(tmp, deltaY);
        return true
    }

    fun update() {
        update(Gdx.graphics.deltaTime)
    }

    fun update(deltaTime: Float) {
        val cont = Controllers.getControllers().first()
        var axis0 = cont.getAxis(0)
        var axis1 = cont.getAxis(1)
        var axis2 = cont.getAxis(2)
        var axis3 = cont.getAxis(3)


        if (axis0 > -dead && axis0 < dead) axis0 = 0f
        if (axis1 > -dead && axis1 < dead) axis1 = 0f
        if (axis2 > -dead && axis2 < dead) axis2 = 0f
        if (axis3 > -dead && axis3 < dead) axis3 = 0f

//        System.out.println("$axis0\t$axis1\t$axis2\t$axis3")

        val deltaX = -axis3 * joylook
        val deltaY = -axis2 * joylook
        if (!(axis2 == 0f && axis3 == 0f)) {
//            camera.direction.rotate(camera.up, deltaX)
            tmp.set(camera.direction).crs(camera.up).nor()
            camera.direction.rotate(tmp, deltaY)
            camera.rotate(deltaX,0f,1f,0f)
        }

        if (keys.containsKey(FORWARD)) {
            tmp.set(camera.direction).nor().scl(deltaTime * velocity)
            camera.position.add(tmp)
        }
        if (keys.containsKey(BACKWARD)) {
            tmp.set(camera.direction).nor().scl(-deltaTime * velocity)
            camera.position.add(tmp)
        }

        if (axis0 != 0f) {
            tmp.set(camera.direction).nor().scl(deltaTime * joyvelocity * -axis0)
            camera.position.add(tmp)
        }

        if (keys.containsKey(STRAFE_LEFT)) {
            tmp.set(camera.direction).crs(camera.up).nor().scl(-deltaTime * velocity)
            camera.position.add(tmp)
        }
        if (keys.containsKey(STRAFE_RIGHT)) {
            tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * velocity)
            camera.position.add(tmp)
        }

        if (axis1 != 0f) {
            tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * joyvelocity * axis1)
            camera.position.add(tmp)
        }

        if (keys.containsKey(UP) || cont.getButton(0)) {
            tmp.set(camera.up).nor().scl(deltaTime * velocity)
            camera.position.add(tmp)
        }
        if (keys.containsKey(DOWN) || cont.getButton(1)) {
            tmp.set(camera.up).nor().scl(-deltaTime * velocity)
            camera.position.add(tmp)
        }
        camera.update(true)
    }
}