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

    int assignVarToLiteral(MethodVertex mv, String varName, String litName, String varType, String litType, int argInd, int lineNo, int order);

    void updateVarName(MethodVertex mv, String oldName, String newName);

}
