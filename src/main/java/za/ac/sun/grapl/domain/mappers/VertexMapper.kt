package za.ac.sun.grapl.domain.mappers

import za.ac.sun.grapl.domain.models.GraPLVertex
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class VertexMapper {

    companion object {
        @JvmStatic
        fun propertiesToMap(objectVertex: GraPLVertex): MutableMap<String, String> {
            val properties = emptyMap<String, String>().toMutableMap()
            properties["label"] = objectVertex.javaClass.getDeclaredField("LABEL").get(objectVertex).toString()
            objectVertex::class.memberProperties.forEach {
                if (it.visibility == KVisibility.PUBLIC) {
                    properties[it.name] = it.getter.call(objectVertex).toString()
                }
            }
            return properties
        }
    }

}