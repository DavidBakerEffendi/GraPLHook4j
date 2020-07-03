package za.ac.sun.grapl.domain.models.vertices

import za.ac.sun.grapl.domain.enums.EvaluationStrategies
import za.ac.sun.grapl.domain.enums.VertexBaseTraits
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.models.GraPLVertex
import java.util.*

/**
 * This node represents a formal parameter going towards the callee side
 */
class MethodParameterInVertex(val code: String, val name: String, val evaluationStrategy: EvaluationStrategies, val typeFullName: String, val lineNumber: Int, val order: Int) : GraPLVertex {
    override fun toString(): String {
        return "MethodParameterInVertex{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", evaluationStrategy=" + evaluationStrategy +
                ", typeFullName='" + typeFullName + '\'' +
                ", lineNumber=" + lineNumber +
                ", order=" + order +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodParameterInVertex

        if (code != other.code) return false
        if (name != other.name) return false
        if (evaluationStrategy != other.evaluationStrategy) return false
        if (typeFullName != other.typeFullName) return false
        if (lineNumber != other.lineNumber) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + evaluationStrategy.hashCode()
        result = 31 * result + typeFullName.hashCode()
        result = 31 * result + lineNumber
        result = 31 * result + order
        return result
    }

    companion object {
        @kotlin.jvm.JvmField
        val LABEL = VertexLabels.METHOD_PARAMETER_IN
        val TRAITS: EnumSet<VertexBaseTraits> = EnumSet.of(VertexBaseTraits.AST_NODE,
                VertexBaseTraits.DECLARATION,
                VertexBaseTraits.CFG_NODE)
    }

}