package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;

/**
 * A reference to e.g. a LOCAL
 */
public class RefEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.REF;

    public RefEdge() {
    }
}
