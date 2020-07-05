package za.ac.sun.grapl.domain.models

import za.ac.sun.grapl.domain.enums.VertexBaseTraits
import za.ac.sun.grapl.domain.enums.VertexLabels
import java.util.*

interface GraPLVertex {
    companion object {
        @JvmField
        val LABEL = VertexLabels.UNKNOWN
        @JvmField
        val TRAITS: EnumSet<VertexBaseTraits> = EnumSet.noneOf(VertexBaseTraits::class.java)
    }
}