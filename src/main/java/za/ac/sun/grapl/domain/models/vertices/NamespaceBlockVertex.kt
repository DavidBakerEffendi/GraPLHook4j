package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A reference to a namespace
 */
public class NamespaceBlockVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.NAMESPACE_BLOCK;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public final String name;
    public final String fullName;
    public final int order;

    public NamespaceBlockVertex(String name, String fullName, int order) {
        this.name = name;
        this.fullName = fullName;
        this.order = order;
    }

    @Override
    public String toString() {
        return "NamespaceBlockVertex{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", order=" + order +
                '}';
    }
}
