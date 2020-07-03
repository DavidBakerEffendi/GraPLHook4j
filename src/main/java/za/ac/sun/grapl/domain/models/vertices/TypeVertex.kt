package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A type which always has to reference a type declaration and may have type argument children if the referred to type
 * declaration is a template
 */
public class TypeVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.TYPE;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.noneOf(VertexBaseTraits.class);

    public final String name;
    public final String fullName;
    public final String typeDeclFullName;

    public TypeVertex(String name, String fullName, String typeDeclFullName) {
        this.name = name;
        this.fullName = fullName;
        this.typeDeclFullName = typeDeclFullName;
    }

    @Override
    public String toString() {
        return "TypeVertex{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", typeDeclFullName='" + typeDeclFullName + '\'' +
                '}';
    }
}
