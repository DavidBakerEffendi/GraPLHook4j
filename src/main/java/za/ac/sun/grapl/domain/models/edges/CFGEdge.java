package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;

/**
 * Control flow edge
 */
public class CFGEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.CFG;

    public CFGEdge() {
    }
}
