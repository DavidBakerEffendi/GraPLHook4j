package za.ac.sun.grapl.domain.models.edges;

import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLEdge;

/**
 * Type argument binding to a type parameter
 */
public class BindsToEdge implements GraPLEdge {
    public static final EdgeLabels LABEL = EdgeLabels.BINDS_TO;

    public BindsToEdge() {
    }
}
