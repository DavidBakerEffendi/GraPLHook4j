package za.ac.sun.grapl.domain.models.vertices

import za.ac.sun.grapl.domain.enums.VertexBaseTraits
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.models.GraPLVertex
import java.util.*

/**
 * Node to save meta data about the graph on its properties. Exactly one node of this type per graph
 */
class MetaDataVertex(val language: String, val version: String) : GraPLVertex {
    override fun toString(): String {
        return "MetaDataVertex{" +
                "language='" + language + '\'' +
                ", version='" + version + '\'' +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MetaDataVertex

        if (language != other.language) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + version.hashCode()
        return result
    }


    companion object {
        @kotlin.jvm.JvmField
        val LABEL = VertexLabels.META_DATA
        val TRAITS: EnumSet<VertexBaseTraits> = EnumSet.noneOf(VertexBaseTraits::class.java)
    }

}