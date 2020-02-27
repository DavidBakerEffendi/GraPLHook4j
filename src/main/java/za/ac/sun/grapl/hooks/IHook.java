package za.ac.sun.grapl.hooks;

import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

public interface IHook {

    void createVertex(GraPLVertex v);

    void createAndAddToMethod(MethodVertex from, MethodParameterInVertex to);

    void createAndAddToMethod(MethodVertex from, MethodReturnVertex to);

    void createAndAddToMethod(MethodVertex from, ModifierVertex to);

    void joinFileVertexTo(FileVertex from, NamespaceBlockVertex to);

    void joinFileVertexTo(FileVertex from, MethodVertex to);

}
