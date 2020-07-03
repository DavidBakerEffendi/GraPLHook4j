package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Literal/Constant
 */
public class LiteralVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.LITERAL;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.EXPRESSION);

    public final String name;
    public final int order;
    public final int argumentIndex;
    public final String typeFullName;
    public final int lineNumber;

    public LiteralVertex(String name, int order, int argumentIndex, String typeFullName, int lineNumber) {
        this.name = name;
        this.order = order;
        this.argumentIndex = argumentIndex;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "LiteralVertex{" +
                "name='" + name + '\'' +
                ", order=" + order +
                ", argumentIndex=" + argumentIndex +
                ", typeFullName='" + typeFullName + '\'' +
                ", lineNumber=" + lineNumber +
                '}';
    }
}
