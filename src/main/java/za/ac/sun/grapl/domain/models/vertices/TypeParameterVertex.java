package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Type parameter of TYPE_DECL or METHOD
 */
public class TypeParameterVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.TYPE_PARAMETER;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public final String name;
    public final int order;

    public TypeParameterVertex(String name, int order) {
        this.name = name;
        this.order = order;
    }
}
