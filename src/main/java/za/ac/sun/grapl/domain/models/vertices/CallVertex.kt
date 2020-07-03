package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.DispatchTypes;
import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A (method)-call
 */
public class CallVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.CALL;
    public static final EnumSet<VertexBaseTraits> TRAITS =
            EnumSet.of(VertexBaseTraits.EXPRESSION,
                    VertexBaseTraits.CALL_REPR);

    public final String code;
    public final String name;
    public final int order;
    public final String methodInstFullName;
    public final String methodFullName;
    public final int argumentIndex;
    public final DispatchTypes dispatchType;
    public final String signature;
    public final String typeFullName;
    public final int lineNumber;

    public CallVertex(String code, String name, int order, String methodInstFullName, String methodFullName,
                      int argumentIndex, DispatchTypes dispatchType, String signature, String typeFullName,
                      int lineNumber) {
        this.code = code;
        this.name = name;
        this.order = order;
        this.methodInstFullName = methodInstFullName;
        this.methodFullName = methodFullName;
        this.argumentIndex = argumentIndex;
        this.dispatchType = dispatchType;
        this.signature = signature;
        this.typeFullName = typeFullName;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "CallVertex{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", methodInstFullName='" + methodInstFullName + '\'' +
                ", methodFullName='" + methodFullName + '\'' +
                ", argumentIndex=" + argumentIndex +
                ", dispatchType=" + dispatchType +
                ", signature='" + signature + '\'' +
                ", typeFullName='" + typeFullName + '\'' +
                ", lineNumber=" + lineNumber +
                '}';
    }
}
