package net.jorhlok.test3d2

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.utils.Array


//starts at back-lf, moves fwd, width & height in quads
class Heightmap(val width: Int, val height: Int, var grid: FloatArray) {
    class Quad ( //btm-lf, top-lf, top-rt, btm-rt, yes?
        val pt0: Vector3 = Vector3(),
        val pt1: Vector3 = Vector3(),
        val pt2: Vector3 = Vector3(),
        val pt3: Vector3 = Vector3()) {
        val normal = Vector3()
        init {
            //calc normal
            val pts = arrayOf(pt0,pt1,pt2,pt3)
            for (i in 0 until pts.size) {
                val cur = pts[i]
                val next = pts[(i+1)%pts.size]
                normal.x += (cur.y-next.y)*(cur.z+next.z)
                normal.y += (cur.z-next.z)*(cur.x+next.x)
                normal.z += (cur.x-next.x)*(cur.y+next.y)
            }
            normal.nor()
//            normal.scl(1f,-1f,1f)
        }
    }

    val quads = Array<Quad>()
    var dasmodel: Model? = null
    var dasinstance: ModelInstance? = null
    var maxh = 0f
    var minh = 0f

    init {
        if (grid.size >= (height+1)*(width+1)) {
            //generate quads
            //make model
            maxh = grid[0]
            minh = grid[0]
            val modelBuilder = ModelBuilder()
            modelBuilder.begin()
            val meshBuilder: MeshPartBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position.toLong() + VertexAttributes.Usage.Normal + VertexAttributes.Usage.ColorPacked, Material())
            var even0 = false
            var even1 = false
            for (z in 0 until height) {
                even1 = even0
                even0 = !even0
                for (x in 0 until width) {
                    maxh = Math.max(maxh,grid[z * (width + 1) + x])
                    minh = Math.min(minh,grid[z * (width + 1) + x])
                    val farquad = Quad(Vector3(x.toFloat(), grid[z * (width + 1) + x], z.toFloat()),
                            Vector3(x.toFloat(), grid[(z + 1) * (width + 1) + x], z + 1f),
                            Vector3(x + 1f, grid[(z + 1) * (width + 1) + x + 1], z + 1f),
                            Vector3(x + 1f, grid[z * (width + 1) + x + 1], z.toFloat()))
                    quads.add(farquad)
                    if (even1) meshBuilder.setColor(0.5f,0.25f,0f,1f)
                    else meshBuilder.setColor(0f,0.5f,0f,1f)
                    even1 = !even1
                    meshBuilder.rect(farquad.pt0,farquad.pt1,farquad.pt2,farquad.pt3,farquad.normal)
                }
            }
            dasmodel = modelBuilder.end()
            dasinstance = ModelInstance(dasmodel)
        }
    }
}