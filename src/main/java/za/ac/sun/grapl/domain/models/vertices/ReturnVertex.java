package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A return instruction
 */
public class ReturnVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.RETURN;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.EXPRESSION);

    public final int lineNumber;
    public final int order;
    public final int argumentIndex;
    public final String code;

    public ReturnVertex(int lineNumber, int order, int argumentIndex, String code) {
        this.lineNumber = lineNumber;
        this.order = order;
        this.argumentIndex = argumentIndex;
        this.code = code;
    }

    @Override
    public String toString() {
        return "ReturnVertex{" +
                "lineNumber=" + lineNumber +
                ", order=" + order +
                ", argumentIndex=" + argumentIndex +
                ", code='" + code + '\'' +
                '}';
    }
}
