package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Member of a class struct or union
 */
public class MemberVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.MEMBER;
    public static final EnumSet<VertexBaseTraits> TRAITS =
            EnumSet.of(VertexBaseTraits.DECLARATION,
                    VertexBaseTraits.AST_NODE);

    public final String code;
    public final String name;
    public final String typeFullName;
    public final int order;

    public MemberVertex(String code, String name, String typeFullName, int order) {
        this.code = code;
        this.name = name;
        this.typeFullName = typeFullName;
        this.order = order;
    }
}
