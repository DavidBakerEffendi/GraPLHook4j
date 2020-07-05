package za.ac.sun.grapl.domain.models.vertices

import za.ac.sun.grapl.domain.enums.VertexBaseTraits
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.models.GraPLVertex
import java.util.*

/**
 * Type parameter of TYPE_DECL or METHOD
 */
class TypeParameterVertex(val name: String, val order: Int) : GraPLVertex {
    override fun toString(): String {
        return "TypeParameterVertex{" +
                "name='" + name + '\'' +
                ", order=" + order +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TypeParameterVertex

        if (name != other.name) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + order
        return result
    }


    companion object {
        @kotlin.jvm.JvmField
        val LABEL = VertexLabels.TYPE_PARAMETER
        val TRAITS: EnumSet<VertexBaseTraits> = EnumSet.of(VertexBaseTraits.AST_NODE)
    }

}