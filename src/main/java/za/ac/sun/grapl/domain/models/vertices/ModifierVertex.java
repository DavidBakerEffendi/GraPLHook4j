package za.ac.sun.grapl.domain.models.vertices;

import za.ac.sun.grapl.domain.enums.ModifierTypes;
import za.ac.sun.grapl.domain.enums.VertexBaseTraits;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

import java.util.EnumSet;

/**
 * A modifier, e.g., static, public, private
 */
public class ModifierVertex implements GraPLVertex {
    public static final VertexLabels LABEL = VertexLabels.MODIFIER;
    public static final EnumSet<VertexBaseTraits> TRAITS = EnumSet.of(VertexBaseTraits.AST_NODE);

    public final ModifierTypes name;
    public final int order;

    public ModifierVertex(ModifierTypes modifierType, int order) {
        this.name = modifierType;
        this.order = order;
    }
}
