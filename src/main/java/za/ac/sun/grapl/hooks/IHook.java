package za.ac.sun.grapl.hooks;

import za.ac.sun.grapl.domain.models.GraPLVertex;

public interface IHook {

    void createVertex(GraPLVertex v);

    boolean putVertexIfAbsent(GraPLVertex v, String key, Object value);

}
