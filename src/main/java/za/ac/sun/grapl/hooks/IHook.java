package za.ac.sun.grapl.hooks;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

public interface IHook {

    /**
     * Creates a {@link Vertex} from a given {@link GraPLVertex}.
     *
     * @param v the {@link GraPLVertex} to translate into a {@link Vertex}.
     */
    void createVertex(GraPLVertex v);

    /**
     * Creates the given {@link MethodParameterInVertex} in the database and joins it to the vertex associated with the
     * given {@link MethodVertex}.
     *
     * @param from the {@link MethodVertex} in the database.
     * @param to   the {@link MethodParameterInVertex} to create and join.
     */
    void createAndAddToMethod(MethodVertex from, MethodParameterInVertex to);

    /**
     * Creates the given {@link MethodReturnVertex} in the database and joins it to the vertex associated with the
     * given {@link MethodVertex}.
     *
     * @param from the {@link MethodVertex} in the database.
     * @param to   the {@link MethodReturnVertex} to create and join.
     */
    void createAndAddToMethod(MethodVertex from, MethodReturnVertex to);

    /**
     * Creates the given {@link ModifierVertex} in the database and joins it to the vertex associated with the
     * given {@link MethodVertex}.
     *
     * @param from the {@link MethodVertex} in the database.
     * @param to   the {@link ModifierVertex} to create and join.
     */
    void createAndAddToMethod(MethodVertex from, ModifierVertex to);

    /**
     * Joins the vertex associated with the given {@link FileVertex} in the database and the vertex associated with the
     * given {@link NamespaceBlockVertex}.
     *
     * @param from the {@link FileVertex} in the database.
     * @param to   the {@link NamespaceBlockVertex} in the database.
     */
    void joinFileVertexTo(FileVertex from, NamespaceBlockVertex to);

    /**
     * Joins the vertex associated with the given {@link FileVertex} in the database and the vertex associated with the
     * given {@link MethodVertex}.
     *
     * @param from the {@link FileVertex} in the database.
     * @param to   the {@link MethodVertex} in the database.
     */
    void joinFileVertexTo(FileVertex from, MethodVertex to);

    /**
     * Creates and assigns the {@link LocalVertex} to the associated {@link BlockVertex} vertex in the database
     * identified by the given {@link MethodVertex} and AST order of the block.
     *
     * @param rootMethod the {@link MethodVertex} which is the root of the search.
     * @param local      the {@link LocalVertex} to associate with the block.
     * @param blockOrder the AST order under which this block occurs.
     */
    void assignToBlock(MethodVertex rootMethod, LocalVertex local, int blockOrder);

    /**
     * Creates and assigns the {@link LiteralVertex} to the associated {@link BlockVertex} vertex in the database
     * identified by the given {@link MethodVertex} and AST order of the block.
     *
     * @param rootMethod the {@link MethodVertex} which is the root of the search.
     * @param literal    the {@link LiteralVertex} to associate with the block.
     * @param blockOrder the AST order under which this block occurs.
     */
    void assignToBlock(MethodVertex rootMethod, LiteralVertex literal, int blockOrder);

    /**
     * Creates and assigns the {@link BlockVertex} to the associated {@link BlockVertex} vertex in the database
     * identified by the given {@link MethodVertex} and AST order of the block.
     *
     * @param rootMethod the {@link MethodVertex} which is the root of the search.
     * @param block      the {@link BlockVertex} to associate with the block.
     * @param blockOrder the AST order under which this block occurs.
     */
    void assignToBlock(MethodVertex rootMethod, BlockVertex block, int blockOrder);

}
