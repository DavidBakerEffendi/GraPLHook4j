package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Initialization construct for arrays
 */
public class ArrayInitializerVertex implements GraPLVertex {

    public static final VertexLabels LABEL = VertexLabels.ARRAY_INITIALIZER;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public ArrayInitializerVertex() {
    }

}
