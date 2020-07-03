package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A control structure such as if, while, or for
 */
public class ControlStructureVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.CONTROL_STRUCTURE;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.EXPRESSION);

    public final String name;
    public final int lineNumber;
    public final int order;
    public final int argumentIndex;

    public ControlStructureVertex(String name, int lineNumber, int order, int argumentIndex) {
        this.name = name;
        this.lineNumber = lineNumber;
        this.order = order;
        this.argumentIndex = argumentIndex;
    }

    @Override
    public String toString() {
        return "ControlStructureVertex{" +
                "name='" + name + '\'' +
                ", lineNumber=" + lineNumber +
                ", order=" + order +
                ", argumentIndex=" + argumentIndex +
                '}';
    }
}
