package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A structuring block in the AST
 */
public class BlockVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.BLOCK;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.EXPRESSION);

    public final String code;
    public final int order;
    public final int argumentIndex;
    public final String typeFullName;
    public final int lineNumber;

    public BlockVertex(String code, int order, int argumentIndex, String typeFullName, int lineNumber) {
        this.code = code;
        this.order = order;
        this.argumentIndex = argumentIndex;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
    }
}
