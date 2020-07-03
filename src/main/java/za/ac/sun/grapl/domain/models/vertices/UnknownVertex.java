package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A language-specific node
 */
public class UnknownVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.UNKNOWN;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.EXPRESSION);

    public final String code;
    public final int order;
    public final int argumentIndex;
    public final int lineNumber;
    public final String typeFullName;

    public UnknownVertex(String code, int order, int argumentIndex, int lineNumber, String typeFullName) {
        this.code = code;
        this.order = order;
        this.argumentIndex = argumentIndex;
        this.lineNumber = lineNumber;
        this.typeFullName = typeFullName;
    }

    @Override
    public String toString() {
        return "UnknownVertex{" +
                "code='" + code + '\'' +
                ", order=" + order +
                ", argumentIndex=" + argumentIndex +
                ", lineNumber=" + lineNumber +
                ", typeFullName='" + typeFullName + '\'' +
                '}';
    }
}
