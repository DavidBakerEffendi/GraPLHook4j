package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;
import za.ac.sun.grapl.domain.models.GraPLVertex;

/**
 * A reference to e.g. a LOCAL
 */
public class RefEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.REF;

    public final GraPLVertex inV;
    public final GraPLVertex outV;

    public RefEdge(GraPLVertex inV, GraPLVertex outV) {
        this.inV = inV;
        this.outV = outV;
    }
}
