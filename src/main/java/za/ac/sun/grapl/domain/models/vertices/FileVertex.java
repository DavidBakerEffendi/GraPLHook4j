package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Node representing a source file. Often also the AST root
 */
public class FileVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.FILE;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public final String name;
    public final int order;

    public FileVertex(String name, int order) {
        this.name = name;
        this.order = order;
    }
}
