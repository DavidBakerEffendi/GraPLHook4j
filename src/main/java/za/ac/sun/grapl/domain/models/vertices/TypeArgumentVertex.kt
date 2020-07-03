package za.ac.sun.grapl.domain.models.vertices

import za.ac.sun.grapl.domain.enums.VertexBaseTraits
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.models.GraPLVertex
import java.util.*

/**
 * Argument for a TYPE_PARAMETER that belongs to a TYPE. It binds another TYPE to a TYPE_PARAMETER
 */
class TypeArgumentVertex(val order: Int) : GraPLVertex {
    override fun toString(): String {
        return "TypeArgumentVertex{" +
                "order=" + order +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TypeArgumentVertex

        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        return order
    }

    companion object {
        @kotlin.jvm.JvmField
        val LABEL = VertexLabels.TYPE_ARGUMENT
        val TRAITS: EnumSet<VertexBaseTraits> = EnumSet.of(VertexBaseTraits.AST_NODE)
    }

}