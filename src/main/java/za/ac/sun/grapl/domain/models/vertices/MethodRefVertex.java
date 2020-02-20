package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Reference to a method instance
 */
public class MethodRefVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.METHOD_REF;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.EXPRESSION);

    public final String code;
    public final int order;
    public final int argumentIndex;
    public final String methodInstFullName;
    public final String methodFullName;
    public final int lineNumber;

    public MethodRefVertex(String code, int order, int argumentIndex, String methodInstFullName, String methodFullName, int lineNumber) {
        this.code = code;
        this.order = order;
        this.argumentIndex = argumentIndex;
        this.methodInstFullName = methodInstFullName;
        this.methodFullName = methodFullName;
        this.lineNumber = lineNumber;
    }
}
