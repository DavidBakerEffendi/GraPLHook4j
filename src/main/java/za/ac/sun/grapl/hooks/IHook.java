package za.ac.sun.grapl.hooks;

import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.MethodParameterInVertex;
import za.ac.sun.grapl.domain.models.vertices.MethodReturnVertex;
import za.ac.sun.grapl.domain.models.vertices.MethodVertex;
import za.ac.sun.grapl.domain.models.vertices.ModifierVertex;

public interface IHook {

    void createVertex(GraPLVertex v);

    boolean putVertexIfAbsent(GraPLVertex v, String key, Object value);

    void createAndAddToMethod(MethodVertex from, MethodParameterInVertex to);

    void createAndAddToMethod(MethodVertex from, MethodReturnVertex to);

    void createAndAddToMethod(MethodVertex from, ModifierVertex to);

}
