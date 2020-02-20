package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A type declaration
 */
public class TypeDeclVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.TYPE_DECL;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public final String name;
    public final String fullName;
    public final String typeDeclFullName;

    public TypeDeclVertex(String name, String fullName, String typeDeclFullName) {
        this.name = name;
        this.fullName = fullName;
        this.typeDeclFullName = typeDeclFullName;
    }
}
