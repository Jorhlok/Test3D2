package net.jorhlok.test3d2

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ConeShapeBuilder
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Pointer {
    var Pos = Vector3()
    var Nor = Vector3(0f,1f,0f)
    var floor: Heightmap? = null
    var dasmodel: Model? = null
    var dasinstance: ModelInstance? = null
    var rotspeed = -0.25f
    var rot = 0f
    var radius = 0.333333f
    var height = 1f
    var divs = 5

    init {
        val mat = Material(ColorAttribute.createDiffuse(0f,0f,1f,1f),BlendingAttribute(0.5f))
        val attr = VertexAttributes.Usage.Position.toLong()// or VertexAttributes.Usage.Normal
        val modelBuilder = ModelBuilder()
        modelBuilder.begin()

        val node0 = modelBuilder.node()
        node0.id = "node0"
        node0.rotation.setEulerAngles(0f,0f,180f)
        node0.translation.set(0f,height/2,0f)
        val meshBuilder: MeshPartBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, attr, mat)

        ConeShapeBuilder.build(meshBuilder,radius*2,height, radius*2,divs)

        dasmodel = modelBuilder.end()
        dasinstance = ModelInstance(dasmodel)
    }

    fun update(deltatime: Float = 0f, p: Vector2 = Vector2()) {
        Pos.x -= p.x
        Pos.z += p.y
        rot += deltatime*360*rotspeed
        if (floor != null) {
            if (Pos.x >= 0 && Pos.z >= 0) {
                val quad = floor!!.quads[(Math.floor(Pos.z.toDouble()).toInt() * (floor!!.width)) + Pos.x.toInt()]
//                System.out.println("${Pos.x}\t${Pos.z}\t\t${quad.pt0.x}\t${quad.pt0.z}")
                Pos.y = quad.interpolate(Pos.x,Pos.z)
                Nor.set(quad.normal)
            }
        }
        dasinstance!!.transform.setToTranslation(Pos).rotate(Quaternion().setFromCross(Vector3(0f,1f,0f),Nor)).rotate(Quaternion().setEulerAngles(rot,0f,0f))
    }
}