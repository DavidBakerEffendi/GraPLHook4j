package za.ac.sun.grapl.domain.models;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;

import java.util.EnumSet;

public interface GraPLVertex {

    VertexLabels LABEL = VertexLabels.UNKNOWN;
    EnumSet<VertexBaseTraits> TRAITS = EnumSet.noneOf(VertexBaseTraits.class);

}
