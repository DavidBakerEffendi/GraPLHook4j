package za.ac.sun.grapl.hooks;

import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

public interface IHook {

    void createVertex(GraPLVertex v);

    void getVertex(Object identifier, VertexLabels label);

    boolean putVertexIfAbsent(GraPLVertex v, String key, Object value);

}
