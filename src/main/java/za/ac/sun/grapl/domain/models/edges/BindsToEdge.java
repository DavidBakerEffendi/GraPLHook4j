package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;
import za.ac.sun.grapl.domain.models.GraPLVertex;

/**
 * Type argument binding to a type parameter
 */
public class BindsToEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.BINDS_TO;

    public final GraPLVertex inV;
    public final GraPLVertex outV;

    public BindsToEdge(GraPLVertex inV, GraPLVertex outV) {
        this.inV = inV;
        this.outV = outV;
    }
}
