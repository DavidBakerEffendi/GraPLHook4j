package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * Node to save meta data about the graph on its properties. Exactly one node of this type per graph
 */
public class MetaDataVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.META_DATA;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.noneOf(VertexBaseTraits.class);

    public final String language;
    public final String version;

    public MetaDataVertex(String language, String version) {
        this.language = language;
        this.version = version;
    }
}
