package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A method/function/procedure
 */
public class MethodVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.METHOD;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(
            VertexBaseTraits.AST_NODE,
            VertexBaseTraits.DECLARATION,
            VertexBaseTraits.CFG_NODE);

    public final String name;
    public final String fullName;
    public final String signature;
    public final int lineNumber;
    public final int order;

    public MethodVertex(String name, String fullName, String signature, int lineNumber, int order) {
        this.name = name;
        this.fullName = fullName;
        this.signature = signature;
        this.lineNumber = lineNumber;
        this.order = order;
    }

    @Override
    public String toString() {
        return "MethodVertex{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", signature='" + signature + '\'' +
                ", lineNumber=" + lineNumber +
                ", order=" + order +
                '}';
    }
}
