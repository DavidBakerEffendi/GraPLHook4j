package za.ac.sun.grapl.hooks;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import za.ac.sun.grapl.domain.models.vertices.*;

public interface IHook {

    /**
     * Creates a {@link Vertex} from a given {@link FileVertex}.
     *
     * @param v the {@link FileVertex} to translate into a {@link Vertex}.
     */
    void addFileVertex(FileVertex v);

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
     * given {@link NamespaceBlockVertex}. If there is no associated vertices then they will be created.
     *
     * @param to   the {@link FileVertex} in the database.
     * @param from the {@link NamespaceBlockVertex} in the database.
     */
    void joinFileVertexTo(FileVertex to, NamespaceBlockVertex from);

    /**
     * Joins the vertex associated with the given {@link FileVertex} in the database and the vertex associated with the
     * given {@link MethodVertex}. If there is no associated vertices then they will be created.
     *
     * @param from the {@link FileVertex} in the database.
     * @param to   the {@link MethodVertex} in the database.
     */
    void joinFileVertexTo(FileVertex from, MethodVertex to);

    /**
     * Joins two namespace block vertices associated with the given {@link NamespaceBlockVertex} parameters. If one or
     * both of the {@link NamespaceBlockVertex} parameters do no have an associated vertex in the database, they are
     * created.
     *
     * @param from the from vertex.
     * @param to   the to vertex.
     */
    void joinNamespaceBlocks(NamespaceBlockVertex from, NamespaceBlockVertex to);

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

    /**
     * Creates and assigns the {@link ControlStructureVertex} to the associated {@link BlockVertex} vertex in the
     * database identified by the given {@link MethodVertex} and AST order of the block.
     *
     * @param rootMethod the {@link MethodVertex} which is the root of the search.
     * @param control    the {@link ControlStructureVertex} to associate with the block.
     * @param blockOrder the AST order under which this block occurs.
     */
    void assignToBlock(MethodVertex rootMethod, ControlStructureVertex control, int blockOrder);

    /**
     * Updates a key-value pair on a {@link BlockVertex} in the database identified by the given {@link MethodVertex}
     * and AST order of the block.
     *
     * @param rootMethod the {@link MethodVertex} which is the root of the search.
     * @param blockOrder the AST order under which this block occurs.
     * @param key        the key of the property to upsert.
     * @param value      the value to upsert the key with.
     */
    void updateBlockProperty(MethodVertex rootMethod, int blockOrder, String key, String value);

    /**
     * Creates a free-floating {@link BlockVertex}
     *
     * @param block the {@link BlockVertex} to create.
     */
    void createFreeBlock(BlockVertex block);

    /**
     * Creates an edge between two {@link BlockVertex} objects.
     *
     * @param blockFrom AST order of the from block.
     * @param blockTo   AST order of the to block.
     */
    void joinBlocks(int blockFrom, int blockTo);

    /**
     * Checked if there is an edge between two {@link BlockVertex} objects.
     *  @param blockFrom AST order of the from block.
     * @param blockTo   AST order of the to block.
     * @return
     */
    boolean areBlocksJoined(int blockFrom, int blockTo);

    /**
     * Traverses the AST nodes to search for the largest order value.
     *
     * @return the largest order property.
     */
    int maxOrder();

    /**
     * Searches for a {@link BlockVertex} associated with this order.
     *
     * @param blockOrder the {@link BlockVertex} order.
     * @return true if there is a {@link BlockVertex} with this order value, false if otherwise.
     */
    boolean isBlock(int blockOrder);

    /**
     * Clears the current loaded graph of all vertices and edges.
     */
    void clearGraph();

    /**
     * Closes the connection to the graph database.
     */
    void close();

    /**
     * Exports the current graph to the serialized format specified by the extension in exportDir
     * path, to the path specified under exportDir.
     *
     * @param exportDir The file to export the graph to.
     */
    void exportCurrentGraph(String exportDir);
}
