package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A binding of a METHOD into a TYPE_DECL
 */
public class BindingVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.BINDING;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.noneOf(VertexBaseTraits.class);

    public final String name;
    public final String signature;

    public BindingVertex(String name, String signature) {
        this.name = name;
        this.signature = signature;
    }
}
