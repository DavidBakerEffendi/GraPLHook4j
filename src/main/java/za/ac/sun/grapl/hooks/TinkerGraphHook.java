package za.ac.sun.grapl.hooks;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inV;

public class TinkerGraphHook implements IHook {

    private static Logger log = LogManager.getLogger();

    private final Graph graph;
    private final String dir;

    private TinkerGraphHook(TinkerGraphHookBuilder builder) {
        BaseConfiguration conf = new BaseConfiguration();
        conf.setProperty("gremlin.graph", "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph");
        this.graph = TinkerGraph.open(conf);
        if (!builder.createNewGraph) graph.traversal().io(builder.graphDir).read().iterate();
        this.dir = builder.graphDir;
    }

    /**
     * Finds the associated {@link Vertex} in the graph based on the given {@link MethodVertex}.
     *
     * @param from The {@link MethodVertex} to use in the search.
     * @return the associated {@link Vertex}.
     */
    private Vertex findVertex(MethodVertex from) {
        GraphTraversalSource g = graph.traversal();
        return g.V().has(MethodVertex.LABEL.toString(), "fullName", from.fullName)
                .has("signature", from.signature).next();
    }

    /**
     * Finds the associated {@link Vertex} in the graph based on the given {@link FileVertex}.
     *
     * @param from The {@link FileVertex} to use in the search.
     * @return the associated {@link Vertex}.
     */
    private Vertex findVertex(FileVertex from) {
        GraphTraversalSource g = graph.traversal();
        return g.V().has(FileVertex.LABEL.toString(), "name", from.name)
                .has("order", String.valueOf(from.order)).next();
    }

    /**
     * Finds the associated {@link Vertex} in the graph based on the given {@link NamespaceBlockVertex}.
     *
     * @param from The {@link NamespaceBlockVertex} to use in the search.
     * @return the associated {@link Vertex}.
     */
    private Vertex findVertex(NamespaceBlockVertex from) {
        GraphTraversalSource g = graph.traversal();
        return g.V().has(NamespaceBlockVertex.LABEL.toString(), "fullName", from.fullName).next();
    }

    /**
     * Checks if there is an associated {@link Vertex} with the given {@link NamespaceBlockVertex}.
     *
     * @param v the {@link NamespaceBlockVertex} to look up.
     * @return false if there is an associated vertex, true if otherwise.
     */
    private boolean vertexNotPresent(NamespaceBlockVertex v) {
        GraphTraversalSource g = graph.traversal();
        return !g.V().has(NamespaceBlockVertex.LABEL.toString(), "fullName", v.fullName)
                .has("name", v.name).hasNext();
    }

    /**
     * Checks if there is an associated {@link Vertex} with the given {@link FileVertex}.
     *
     * @param v the {@link FileVertex} to look up.
     * @return false if there is an associated vertex, true if otherwise.
     */
    private boolean vertexNotPresent(FileVertex v) {
        GraphTraversalSource g = graph.traversal();
        return !g.V().has(FileVertex.LABEL.toString(), "name", v.name)
                .has("order", String.valueOf(v.order)).hasNext();
    }

    @Override
    public void addFileVertex(FileVertex v) {
        createTinkerGraphVertex(v);
    }

    @Override
    public void createAndAddToMethod(MethodVertex from, MethodParameterInVertex to) {
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, createTinkerGraphVertex(to));
    }

    @Override
    public void createAndAddToMethod(MethodVertex from, MethodReturnVertex to) {
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, createTinkerGraphVertex(to));
    }

    @Override
    public void createAndAddToMethod(MethodVertex from, ModifierVertex to) {
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, createTinkerGraphVertex(to));
    }

    @Override
    public void joinFileVertexTo(FileVertex to, NamespaceBlockVertex from) {
        if (vertexNotPresent(from)) {
            createTinkerGraphVertex(from);
        }
        if (vertexNotPresent(to)) {
            createTinkerGraphVertex(to);
        }
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, findVertex(to));
    }

    @Override
    public void joinFileVertexTo(FileVertex from, MethodVertex to) {
        if (vertexNotPresent(from)) createTinkerGraphVertex(from);
        if (!graph.traversal().V(findVertex(from))
                .out(EdgeLabels.AST.toString())
                .has("fullName", to.fullName)
                .has("signature", to.signature)
                .hasNext()) {
            createTinkerGraphVertex(to);
        }
        createTinkerGraphEdge(findVertex(from), EdgeLabels.AST, findVertex(to));
    }

    @Override
    public void joinNamespaceBlocks(NamespaceBlockVertex from, NamespaceBlockVertex to) {
        if (vertexNotPresent(from)) createTinkerGraphVertex(from);
        if (vertexNotPresent(to)) createTinkerGraphVertex(to);
        Vertex n1 = findVertex(from);
        Vertex n2 = findVertex(to);
        if (!graph.traversal().V(n1).outE(EdgeLabels.AST.toString()).filter(inV().is(n2)).hasNext()) {
            createTinkerGraphEdge(n1, EdgeLabels.AST, n2);
        }
    }

    @Override
    public void assignToBlock(MethodVertex rootMethod, LocalVertex local, int blockOrder) {
        createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(local));
    }

    @Override
    public void assignToBlock(MethodVertex rootMethod, LiteralVertex literal, int blockOrder) {
        createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(literal));
    }

    @Override
    public void assignToBlock(MethodVertex rootMethod, BlockVertex block, int blockOrder) {
        if (blockOrder == 0) {
            createTinkerGraphEdge(findVertex(rootMethod), EdgeLabels.AST, createTinkerGraphVertex(block));
        } else {
            createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(block));
        }
    }

    @Override
    public void assignToBlock(MethodVertex rootMethod, ControlStructureVertex control, int blockOrder) {
        createTinkerGraphEdge(findBlock(rootMethod, blockOrder), EdgeLabels.AST, createTinkerGraphVertex(control));
    }

    /**
     * Finds the associated {@link Vertex} in the graph to the block based on the {@link MethodVertex} and the AST order
     * under which this block occurs under this {@link MethodVertex}.
     *
     * @param root       the {@link MethodVertex} which is the root of the search.
     * @param blockOrder the AST order under which this block occurs.
     * @return the {@link Vertex} associated with the AST block.
     */
    private Vertex findBlock(MethodVertex root, int blockOrder) {
        GraphTraversalSource g = graph.traversal();
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
    private Vertex createTinkerGraphVertex(GraPLVertex gv) {
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
        final Vertex v = this.graph.addVertex(T.label, label, T.id, UUID.randomUUID());
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
    private Edge createTinkerGraphEdge(Vertex v1, EdgeLabels edgeLabel, Vertex v2) {
        return v1.addEdge(edgeLabel.toString(), v2, T.id, UUID.randomUUID());
    }

    /**
     * Exports the current graph to the serialized format specified by the extension in the {@link TinkerGraphHook#dir}
     * path, to the path specified under {@link TinkerGraphHook#dir}.
     */
    public void exportCurrentGraph() {
        this.graph.traversal().io(dir).write().iterate();
    }

    public static class TinkerGraphHookBuilder {
        private boolean createNewGraph;
        private String graphDir;

        public TinkerGraphHookBuilder(String graphDir) {
            this.createNewGraph = false;
            String ext = graphDir.substring(graphDir.lastIndexOf('.') + 1).toLowerCase();
            switch (ext) {
                case "xml":
                case "json":
                case "kryo":
                    this.graphDir = graphDir;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported graph extension! Supported types are GraphML," +
                            " GraphSON, and Gryo.");
            }

        }

        public TinkerGraphHookBuilder createNewGraph(boolean createNewGraph) {
            this.createNewGraph = createNewGraph;
            return this;
        }

        public TinkerGraphHook build() {
            return new TinkerGraphHook(this);
        }
    }
}
