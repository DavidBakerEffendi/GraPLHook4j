package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Argument for a TYPE_PARAMETER that belongs to a TYPE. It binds another TYPE to a TYPE_PARAMETER
 */
public class TypeArgumentVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.TYPE_ARGUMENT;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public final int order;

    public TypeArgumentVertex(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "TypeArgumentVertex{" +
                "order=" + order +
                '}';
    }
}
