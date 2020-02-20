package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;
import za.ac.sun.grapl.domain.models.GraPLVertex;

/**
 * Syntax tree edge
 */
public class ASTEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.AST;

    public final GraPLVertex inV;
    public final GraPLVertex outV;

    public ASTEdge(GraPLVertex inV, GraPLVertex outV) {
        this.inV = inV;
        this.outV = outV;
    }
}
