package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.EvaluationStrategies;
import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * This node represents a formal parameter going towards the callee side
 */
public class MethodParameterInVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.METHOD_PARAMETER_IN;
    public static final EnumSet<VertexBaseTraits> TRAITS =
            EnumSet.of(VertexBaseTraits.AST_NODE,
                    VertexBaseTraits.DECLARATION,
                    VertexBaseTraits.CFG_NODE);

    public final String code;
    public final String name;
    public final EvaluationStrategies evaluationStrategy;
    public final String typeFullName;
    public final int lineNumber;
    public final int order;

    public MethodParameterInVertex(String code, String name, EvaluationStrategies evaluationStrategy, String typeFullName, int lineNumber, int order) {
        this.code = code;
        this.name = name;
        this.evaluationStrategy = evaluationStrategy;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
        this.order = order;
    }

    @Override
    public String toString() {
        return "MethodParameterInVertex{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", evaluationStrategy=" + evaluationStrategy +
                ", typeFullName='" + typeFullName + '\'' +
                ", lineNumber=" + lineNumber +
                ", order=" + order +
                '}';
    }
}
