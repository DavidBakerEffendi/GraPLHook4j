package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;

/**
 * Syntax tree edge
 */
public class ASTEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.AST;

    public ASTEdge() {
    }
}
