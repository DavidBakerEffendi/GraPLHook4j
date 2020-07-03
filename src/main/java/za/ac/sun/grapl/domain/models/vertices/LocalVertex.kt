package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A local variable
 */
public class LocalVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.LOCAL;
    public static final EnumSet<VertexBaseTraits> TRAITS =
            EnumSet.of(VertexBaseTraits.DECLARATION,
                    VertexBaseTraits.LOCAL_LIKE,
                    VertexBaseTraits.CALL_REPR);

    public final String code;
    public final String name;
    public final String typeFullName;
    public final int lineNumber;
    public final int order;

    public LocalVertex(String code, String name, String typeFullName, int lineNumber, int order) {
        this.code = code;
        this.name = name;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
        this.order = order;
    }

    @Override
    public String toString() {
        return "LocalVertex{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", typeFullName='" + typeFullName + '\'' +
                ", lineNumber=" + lineNumber +
                ", order=" + order +
                '}';
    }
}
