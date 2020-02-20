package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;
import za.ac.sun.grapl.domain.models.GraPLVertex;

/**
 * Membership relation for a compound object
 */
public class ContainsNodeEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.CONTAINS_NODE;

    public final GraPLVertex inV;
    public final GraPLVertex outV;

    public final String localName;
    public final int index;

    public ContainsNodeEdge(GraPLVertex inV, GraPLVertex outV, String localName, int index) {
        this.inV = inV;
        this.outV = outV;
        this.localName = localName;
        this.index = index;
    }
}
