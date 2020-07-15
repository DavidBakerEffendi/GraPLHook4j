package za.ac.sun.grapl.domain.mappers

import za.ac.sun.grapl.domain.models.GraPLVertex
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class VertexMapper {

    companion object {
        @JvmStatic
        fun propertiesToMap(objectVertex: GraPLVertex): MutableMap<String, Any> {
            val properties = emptyMap<String, Any>().toMutableMap()
            properties["label"] = objectVertex.javaClass.getDeclaredField("LABEL").get(objectVertex).toString()
            objectVertex::class.memberProperties.forEach {
                if (it.visibility == KVisibility.PUBLIC) {
                    if (it.getter.call(objectVertex) is Enum<*>) properties[it.name] = it.getter.call(objectVertex).toString()
                    else properties[it.name] = it.getter.call(objectVertex)!!
                }
            }
            return properties
        }
    }

}