package net.jorhlok.test3d2

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.Vector3

class Heightmap(val width: Int, val height: Int, var grid: FloatArray) {
    class Quad (
        val pt0: Vector3 = Vector3(),
        val pt1: Vector3 = Vector3(),
        val pt2: Vector3 = Vector3(),
        val pt3: Vector3 = Vector3()) {
        val normal = Vector3()
        init {
            //calc normal
        }
    }

    var quads: Array<Quad>? = null
    var dasmodel: Model? = null
    val maxh = 0f
    val minh = 0f

    init {
        //generate quads
        //make model
    }
}