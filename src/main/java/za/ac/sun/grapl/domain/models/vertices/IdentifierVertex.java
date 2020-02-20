package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * An arbitrary identifier/reference
 */
public class IdentifierVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.IDENTIFIER;
    public static final EnumSet<VertexBaseTraits> TRAITS =
            EnumSet.of(VertexBaseTraits.EXPRESSION,
                    VertexBaseTraits.LOCAL_LIKE);

    public final String code;
    public final String name;
    public final int order;
    public final int argumentIndex;
    public final String typeFullName;
    public final int lineNumber;

    public IdentifierVertex(String code, String name, int order, int argumentIndex, String typeFullName, int lineNumber) {
        this.code = code;
        this.name = name;
        this.order = order;
        this.argumentIndex = argumentIndex;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
    }
}
