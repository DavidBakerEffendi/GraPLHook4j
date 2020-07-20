package za.ac.sun.grapl.hooks;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.MethodDescriptorVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

public interface IHook {

    /**
     * Registers the meta-data of the graph's underlying language.
     *
     * @param vertex vertex holding the language and version information.
     */
    void registerMetaData(final MetaDataVertex vertex);

    /**
     * Creates a {@link Vertex} from a given {@link FileVertex}.
     *
     * @param v the {@link FileVertex} to translate into a {@link Vertex}.
     */
    void addFileVertex(final FileVertex v);

    /**
     * Creates the given {@link MethodDescriptorVertex} in the database and joins it to the vertex associated with the
     * given {@link MethodVertex}.
     *
     * @param from the {@link MethodVertex} in the database.
     * @param to   the {@link MethodDescriptorVertex} to create and join.
     */
    void createAndAddToMethod(final MethodVertex from, final MethodDescriptorVertex to);

    /**
     * Creates the given {@link ModifierVertex} in the database and joins it to the vertex associated with the
     * given {@link MethodVertex}.
     *
     * @param from the {@link MethodVertex} in the database.
     * @param to   the {@link ModifierVertex} to create and join.
     */
    void createAndAddToMethod(final MethodVertex from, final ModifierVertex to);

    /**
     * Joins the vertex associated with the given {@link FileVertex} in the database and the vertex associated with the
     * given {@link NamespaceBlockVertex}. If there is no associated vertices then they will be created.
     *
     * @param to   the {@link FileVertex} in the database.
     * @param from the {@link NamespaceBlockVertex} in the database.
     */
    void joinFileVertexTo(final FileVertex to, final NamespaceBlockVertex from);

    /**
     * Joins the vertex associated with the given {@link FileVertex} in the database and the vertex associated with the
     * given {@link MethodVertex}. If there is no associated vertices then they will be created.
     *
     * @param from the {@link FileVertex} in the database.
     * @param to   the {@link MethodVertex} in the database.
     */
    void joinFileVertexTo(final FileVertex from, final MethodVertex to);

    /**
     * Joins two namespace block vertices associated with the given {@link NamespaceBlockVertex} parameters. If one or
     * both of the {@link NamespaceBlockVertex} parameters do no have an associated vertex in the database, they are
     * created.
     *
     * @param from the from vertex.
     * @param to   the to vertex.
     */
    void joinNamespaceBlocks(final NamespaceBlockVertex from, final NamespaceBlockVertex to);

    /**
     * Creates and assigns the {@link GraPLVertex} to the associated {@link MethodVertex} vertex in the
     * database identified by the given {@link MethodVertex}.
     *
     * @param parentVertex the {@link MethodVertex} to connect.
     * @param newVertex    the {@link GraPLVertex} to associate with the block.
     */
    void createAndAssignToBlock(final MethodVertex parentVertex, final GraPLVertex newVertex);

    /**
     * Creates and assigns the {@link GraPLVertex} to the associated {@link BlockVertex} vertex in the
     * database identified by the given {@link MethodVertex} and AST order of the block.
     *
     * @param rootVertex the {@link MethodVertex} which is the root of the search.
     * @param newVertex  the {@link GraPLVertex} to associate with the block.
     * @param blockOrder the AST order under which this block occurs.
     */
    void createAndAssignToBlock(final MethodVertex rootVertex, final GraPLVertex newVertex, final int blockOrder);

    /**
     * Creates and assigns the {@link GraPLVertex} to the associated {@link BlockVertex} vertex in the
     * database identified purely by the order.
     *
     * @param newVertex  the {@link GraPLVertex} to associate with the block.
     * @param blockOrder the AST order under which this block occurs.
     */
    void createAndAssignToBlock(final GraPLVertex newVertex, final int blockOrder);

    /**
     * Updates a key-value pair on a {@link GraPLVertex} in the database identified by the given {@link MethodVertex}
     * and AST order of the block.
     *
     * @param rootVertex the {@link MethodVertex} which is the root of the search.
     * @param order      the AST order under which this block occurs.
     * @param key        the key of the property to upsert.
     * @param value      the value to upsert the key with.
     */
    void updateASTVertexProperty(final MethodVertex rootVertex, int order, String key, String value);

    /**
     * Updates a key-value pair on a {@link GraPLVertex} in the database identified by the given AST order of the block.
     *
     * @param order the AST order under which this block occurs.
     * @param key   the key of the property to upsert.
     * @param value the value to upsert the key with.
     */
    void updateASTVertexProperty(final int order, final String key, final String value);

    /**
     * Creates a free-floating {@link GraPLVertex}
     *
     * @param graPLVertex the {@link GraPLVertex} to create.
     */
    void createVertex(final GraPLVertex graPLVertex);

    /**
     * Creates an edge between two {@link BlockVertex} objects.
     *
     * @param blockFrom AST order of the from block.
     * @param blockTo   AST order of the to block.
     * @param edgeLabel The label to be attached to the edge.
     */
    void joinASTVerticesByOrder(final int blockFrom, final int blockTo, final EdgeLabels edgeLabel);

    /**
     * Checked if there is an edge between two {@link BlockVertex} objects.
     *
     * @param blockFrom AST order of the from block.
     * @param blockTo   AST order of the to block.
     * @param edgeLabel The label to be attached to the edge.
     * @return true if joined by an edge, false if otherwise.
     */
    boolean areASTVerticesJoinedByEdge(final int blockFrom, final int blockTo, final EdgeLabels edgeLabel);

    /**
     * Traverses the AST nodes to search for the largest order value.
     *
     * @return the largest order property.
     */
    int maxOrder();

    /**
     * Searches for a {@link GraPLVertex} associated with this order.
     *
     * @param blockOrder the {@link GraPLVertex} order.
     * @return true if there is a {@link GraPLVertex} with this order value, false if otherwise.
     */
    boolean isASTVertex(final int blockOrder);

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
    void exportCurrentGraph(final String exportDir);
}
