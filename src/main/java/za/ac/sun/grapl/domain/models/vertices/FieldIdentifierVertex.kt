package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A node that represents which field is accessed in a <operator>.fieldAccess, in e.g. obj.field. The CODE part is used
 * for human display and matching to MEM?BER nodes. The CANONICAL_NAME is used for dataflow tracking; typically both
 * coincide. However, suppose that two fields foo and bar are a C-style union; then CODE refers to whatever the
 * programmer wrote (obj.foo or obj.bar), but both share the same CANONICAL_NAME (e.g. GENERATED_foo_bar)
 */
public class FieldIdentifierVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.FIELD_IDENTIFIER;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.EXPRESSION);

    public final String code;
    public final String canonicalName;
    public final int order;
    public final int argumentIndex;
    public final int lineNumber;

    public FieldIdentifierVertex(String code, String canonicalName, int order, int argumentIndex, int lineNumber) {
        this.code = code;
        this.canonicalName = canonicalName;
        this.order = order;
        this.argumentIndex = argumentIndex;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "FieldIdentifierVertex{" +
                "code='" + code + '\'' +
                ", canonicalName='" + canonicalName + '\'' +
                ", order=" + order +
                ", argumentIndex=" + argumentIndex +
                ", lineNumber=" + lineNumber +
                '}';
    }
}
