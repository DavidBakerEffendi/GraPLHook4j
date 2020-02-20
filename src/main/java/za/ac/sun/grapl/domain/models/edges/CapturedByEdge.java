package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;

/**
 * Connection between a captured LOCAL and the corresponding CLOSURE_BINDING
 */
public class CapturedByEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.CAPTURED_BY;

    public CapturedByEdge() {
    }
}
