package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;
import za.ac.sun.grapl.domain.models.GraPLVertex;

/**
 * Control flow edge
 */
public class CFGEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.CFG;

    public final GraPLVertex inV;
    public final GraPLVertex outV;

    public CFGEdge(GraPLVertex inV, GraPLVertex outV) {
        this.inV = inV;
        this.outV = outV;
    }
}
