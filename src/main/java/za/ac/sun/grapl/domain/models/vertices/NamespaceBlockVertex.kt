package za.ac.sun.grapl.domain.models.vertices

import za.ac.sun.grapl.domain.enums.VertexBaseTraits
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.models.GraPLVertex
import java.util.*

/**
 * A reference to a namespace
 */
class NamespaceBlockVertex(val name: String, val fullName: String, val order: Int) : GraPLVertex {
    override fun toString(): String {
        return "NamespaceBlockVertex{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", order=" + order +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NamespaceBlockVertex

        if (name != other.name) return false
        if (fullName != other.fullName) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + order
        return result
    }

    companion object {
        @kotlin.jvm.JvmField
        val LABEL = VertexLabels.NAMESPACE_BLOCK
        val TRAITS: EnumSet<VertexBaseTraits> = EnumSet.of(VertexBaseTraits.AST_NODE)
    }

}