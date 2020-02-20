package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.EvaluationStrategies;
import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A modifier, e.g., static, public, private
 */
public class ModifierVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.MODIFIER;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public final EvaluationStrategies evaluationStrategy;
    public final String typeFullName;
    public final int lineNumber;
    public final int order;

    public ModifierVertex(EvaluationStrategies evaluationStrategy, String typeFullName, int lineNumber, int order) {
        this.evaluationStrategy = evaluationStrategy;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
        this.order = order;
    }
}
