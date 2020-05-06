package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inV;

public abstract class GremlinHook implements IHook {

    private static Logger log = LogManager.getLogger();
    final Graph graph;
    private GraphTraversalSource g;

    public GremlinHook(final Graph graph) {
        this.graph = graph;
    }

    protected void startTransaction() {
        g = graph.traversal();
    }

    protected void endTransaction() {
        try {
            g.close();
        } catch (Exception e) {
            log.warn("Unable to close existing transaction! Object will be orphaned and a new traversal will continue.");
        }
    }

    protected Graph getGraph() {
        return graph;
    }

    /**
     * Finds the associated {@link Vertex} in the graph based on the given {@link MethodVertex}.
     *
     * @param from The {@link MethodVertex} to use in the search.
     * @return the associated {@link Vertex}.
     */
    private Vertex findVertex(final MethodVertex from) {
        startTransaction();
        final Vertex result = g.V().has(MethodVertex.LABEL.toString(), "fullName", from.fullName)
                .has("signature", from.signature).next();
        endTransaction();
        return result;
    }

    /**
     * Finds the associated {@link Vertex} in the graph based on the given {@link FileVertex}.
     *
     * @param from The {@link FileVertex} to use in the search.
     * @return the associated {@link Vertex}.
     */
    private Vertex findVertex(final FileVertex from) {
        startTransaction();
        final Vertex result = g.V().has(FileVertex.LABEL.toString(), "name", from.name)
                .has("order", String.valueOf(from.order)).next();
        endTransaction();
        return result;
    }

    /**
     * Finds the associated {@link Vertex} in the graph based on the given {@link NamespaceBlockVertex}.
     *
     * @param from The {@link NamespaceBlockVertex} to use in the search.
     * @return the associated {@link Vertex}.
     */
    private Vertex findVertex(final NamespaceBlockVertex from) {
        startTransaction();
        final Vertex result = g.V().has(NamespaceBlockVertex.LABEL.toString(), "fullName", from.fullName).next();
        endTransaction();
        return result;
    }

    /**
     * Checks if there is an associated {@link Vertex} with the given {@link NamespaceBlockVertex}.
     *
     * @param v the {@link NamespaceBlockVertex} to look up.
     * @return false if there is an associated vertex, true if otherwise.
     */
    private boolean vertexNotPresent(final NamespaceBlockVertex v) {
        startTransaction();
        final boolean result = !g.V().has(NamespaceBlockVertex.LABEL.toString(), "fullName", v.fullName)
                .has("name", v.name).hasNext();
        endTransaction();
        return result;
    }

    /**
     * Checks if there is an associated {@link Vertex} with the given {@link FileVertex}.
     *
     * @param v the {@link FileVertex} to look up.
     * @return false if there is an associated vertex, true if otherwise.
     */
    private boolean vertexNotPresent(final FileVertex v) {
        startTransaction();
        final boolean result = !g.V().has(FileVertex.LABEL.toString(), "name", v.name)
                .has("order", String.valueOf(v.order)).hasNext();
        endTransaction();
        return result;
    }

    @Override
    public void addFileVertex(final FileVertex v) {
        startTransaction();
        createTinkerGraphVertex(v);
        endTransaction();
    }

    @Override
    public void createAndAddToMethod(final MethodVertex from, final MethodParameterInVertex to) {
        startTransaction();
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, createTinkerGraphVertex(to));
        endTransaction();
    }

    @Override
    public void createAndAddToMethod(final MethodVertex from, final MethodReturnVertex to) {
        startTransaction();
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, createTinkerGraphVertex(to));
        endTransaction();
    }

    @Override
    public void createAndAddToMethod(final MethodVertex from, final ModifierVertex to) {
        startTransaction();
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, createTinkerGraphVertex(to));
        endTransaction();
    }

    @Override
    public void joinFileVertexTo(final FileVertex to, final NamespaceBlockVertex from) {
        startTransaction();
        if (vertexNotPresent(from)) {
            createTinkerGraphVertex(from);
        }
        if (vertexNotPresent(to)) {
            createTinkerGraphVertex(to);
        }
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, findVertex(to));
        endTransaction();
    }

    @Override
    public void joinFileVertexTo(final FileVertex from, final MethodVertex to) {
        startTransaction();
        if (vertexNotPresent(from)) createTinkerGraphVertex(from);
        if (!graph.traversal().V(findVertex(from))
                .out(EdgeLabels.AST.toString())
                .has("fullName", to.fullName)
                .has("signature", to.signature)
                .hasNext()) {
            createTinkerGraphVertex(to);
        }
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, findVertex(to));
        endTransaction();
    }

    @Override
    public void joinNamespaceBlocks(final NamespaceBlockVertex from, final NamespaceBlockVertex to) {
        startTransaction();
        if (vertexNotPresent(from)) createTinkerGraphVertex(from);
        if (vertexNotPresent(to)) createTinkerGraphVertex(to);
        Vertex n1 = findVertex(from);
        Vertex n2 = findVertex(to);
        if (!graph.traversal().V(n1).outE(EdgeLabels.AST.toString()).filter(inV().is(n2)).hasNext()) {
            createTinkerGraphEdge(n1, EdgeLabels.AST, n2);
        }
        endTransaction();
    }

    @Override
    public void assignToBlock(final MethodVertex rootMethod, final LocalVertex local, final int blockOrder) {
        startTransaction();
        createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(local));
        endTransaction();
    }

    @Override
    public void assignToBlock(final MethodVertex rootMethod, final LiteralVertex literal, final int blockOrder) {
        startTransaction();
        createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(literal));
        endTransaction();
    }

    @Override
    public void assignToBlock(final MethodVertex rootMethod, final BlockVertex block, final int blockOrder) {
        startTransaction();
        if (blockOrder == 0) {
            createTinkerGraphEdge(findVertex(rootMethod), EdgeLabels.AST, createTinkerGraphVertex(block));
        } else {
            createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(block));
        }
        endTransaction();
    }

    @Override
    public void assignToBlock(final MethodVertex rootMethod, final ControlStructureVertex control, final int blockOrder) {
        startTransaction();
        createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(control));
        endTransaction();
    }

    @Override
    public int maxOrder() {
        startTransaction();
        int result = 0;
        if (g.V().has("order").hasNext())
            result = Integer.parseInt(g.V().order().by("order", desc).limit(1).values("order").next().toString());
        endTransaction();
        return result;
    }

    @Override
    public boolean isBlock(final int blockOrder) {
        startTransaction();
        boolean result = g.V().has(BlockVertex.LABEL.toString(), "order", String.valueOf(blockOrder)).hasNext();
        endTransaction();
        return result;
    }

    /**
     * Finds the associated {@link Vertex} in the graph to the block based on the {@link MethodVertex} and the AST order
     * under which this block occurs under this {@link MethodVertex}.
     *
     * @param root       the {@link MethodVertex} which is the root of the search.
     * @param blockOrder the AST order under which this block occurs.
     * @return the {@link Vertex} associated with the AST block.
     */
    private Vertex findBlock(final MethodVertex root, final int blockOrder) {
        startTransaction();
        return g.V(findVertex(root)).repeat(__.out("AST")).emit()
                .has("order", String.valueOf(blockOrder)).next();
    }

    /**
     * Given a {@link GraPLVertex}, creates a {@link Vertex} and translates the object's field properties to key-value
     * pairs on the {@link Vertex} object. This is then added to this hook's {@link Graph}.
     *
     * @param gv the {@link GraPLVertex} to translate into a {@link Vertex}.
     * @return the newly created {@link Vertex}.
     */
    private Vertex createTinkerGraphVertex(final GraPLVertex gv) {
        final Field[] fields = Arrays
                .stream(gv.getClass().getFields())
                .filter((field -> !"LABEL".equals(field.getName()) && !"TRAITS".equals(field.getName())))
                .toArray(Field[]::new);
        // Get the implementing class label parameter
        String label = "UNKNOWN";
        try {
            label = ((VertexLabels) gv.getClass().getField("LABEL").get("LABEL")).name();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        // Get the implementing classes fields and values
        final Vertex v = getGraph().addVertex(T.label, label, T.id, UUID.randomUUID());
        Arrays.stream(fields).forEach(f -> {
            try {
                v.property(f.getName(), f.get(gv).toString());
            } catch (IllegalAccessException e) {
                log.error("Illegal field access when adding properties to '" + gv.LABEL.name() + "'.", e);
            }
        });
        return v;
    }

    /**
     * Wrapper method for creating an edge between two vertices. This wrapper method assigns a random UUID as the ID
     * for the edge.
     *
     * @param v1        the from {@link Vertex}.
     * @param edgeLabel the CPG edge label.
     * @param v2        the to {@link Vertex}.
     * @return the newly created {@link Edge}.
     */
    private Edge createTinkerGraphEdge(final Vertex v1, final EdgeLabels edgeLabel, final Vertex v2) {
        return v1.addEdge(edgeLabel.toString(), v2, T.id, UUID.randomUUID());
    }

}
