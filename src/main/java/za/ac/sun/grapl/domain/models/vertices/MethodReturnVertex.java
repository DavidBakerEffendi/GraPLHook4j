package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.EvaluationStrategies;
import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A formal method return
 */
public class MethodReturnVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.METHOD_RETURN;
    public static final EnumSet<VertexBaseTraits> TRAITS =
            EnumSet.of(VertexBaseTraits.CFG_NODE,
                    VertexBaseTraits.TRACKING_POINT);

    public final String name;
    public final EvaluationStrategies evaluationStrategy;
    public final String typeFullName;
    public final int lineNumber;
    public final int order;

    public MethodReturnVertex(String name, String typeFullName, EvaluationStrategies evaluationStrategy, int lineNumber, int order) {
        this.name = name;
        this.evaluationStrategy = evaluationStrategy;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
        this.order = order;
    }

    @Override
    public String toString() {
        return "MethodReturnVertex{" +
                "name='" + name + '\'' +
                ", evaluationStrategy=" + evaluationStrategy +
                ", typeFullName='" + typeFullName + '\'' +
                ", lineNumber=" + lineNumber +
                ", order=" + order +
                '}';
    }
}
