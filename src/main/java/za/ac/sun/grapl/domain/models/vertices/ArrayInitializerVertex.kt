package za.ac.sun.grapl.domain.models.vertices

import za.ac.sun.grapl.domain.enums.VertexBaseTraits
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.models.ASTVertex
import za.ac.sun.grapl.domain.models.GraPLVertex
import java.util.*

/**
 * Initialization construct for arrays
 */
class ArrayInitializerVertex(order: Int) : ASTVertex(order) {
    override fun toString(): String {
        return "ArrayInitializerVertex{order=$order}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayInitializerVertex

        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        return order
    }

    companion object {
        @kotlin.jvm.JvmField
        val LABEL = VertexLabels.ARRAY_INITIALIZER
        val TRAITS: EnumSet<VertexBaseTraits> = EnumSet.of(VertexBaseTraits.AST_NODE)
    }
}