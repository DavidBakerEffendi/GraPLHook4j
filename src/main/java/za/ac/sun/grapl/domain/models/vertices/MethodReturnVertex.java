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

    public final String code;
    public final EvaluationStrategies evaluationStrategy;
    public final String typeFullName;
    public final int lineNumber;
    public final int order;

    public MethodReturnVertex(String code, EvaluationStrategies evaluationStrategy, String typeFullName, int lineNumber, int order) {
        this.code = code;
        this.evaluationStrategy = evaluationStrategy;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
        this.order = order;
    }
}
