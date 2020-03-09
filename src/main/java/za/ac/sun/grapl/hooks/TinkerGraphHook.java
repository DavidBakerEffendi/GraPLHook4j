package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TinkerGraphHook implements IHook {

    private static Logger log = LogManager.getLogger();

    private final Graph graph;
    private final String dir;

    private TinkerGraphHook(TinkerGraphHookBuilder builder) {
        if (builder.createNewGraph) {
            this.graph = TinkerGraph.open();
        } else {
            this.graph = TinkerGraphHook.importGraph(builder.graphDir);
        }
        this.dir = builder.graphDir;
    }

    /**
     * Imports an existing TinkerGraph from the specified directory. Supported formats are GraphML, GraphSON, and Gryo.
     *
     * @param dir the path to the existing TinkerGraph.
     * @return the deserialized {@link Graph} object.
     */
    private static Graph importGraph(String dir) {
        TinkerGraph g = TinkerGraph.open();
        g.traversal().io(dir).read().iterate();
        return g;
    }

    @Override
    public void createVertex(GraPLVertex v) {
        createTinkerGraphVertex(v);
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
        return g.V().has(FileVertex.LABEL.toString(), "name", from.name).next();
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

    @Override
    public void createAndAddToMethod(MethodVertex from, MethodParameterInVertex to) {
        findVertex(from).addEdge(EdgeLabels.AST.toString(), createTinkerGraphVertex(to));
    }

    @Override
    public void createAndAddToMethod(MethodVertex from, MethodReturnVertex to) {
        findVertex(from).addEdge(EdgeLabels.AST.toString(), createTinkerGraphVertex(to));
    }

    @Override
    public void createAndAddToMethod(MethodVertex from, ModifierVertex to) {
        findVertex(from).addEdge(EdgeLabels.AST.toString(), createTinkerGraphVertex(to));
    }

    @Override
    public void joinFileVertexTo(FileVertex from, NamespaceBlockVertex to) {
        findVertex(from).addEdge(EdgeLabels.AST.toString(), findVertex(to));
    }

    @Override
    public void joinFileVertexTo(FileVertex from, MethodVertex to) {
        findVertex(from).addEdge(EdgeLabels.AST.toString(), findVertex(to));
    }

    @Override
    public void assignToBlock(MethodVertex rootMethod, LocalVertex local, int blockOrder) {
        findBlock(rootMethod, blockOrder).addEdge("AST", createTinkerGraphVertex(local));
    }

    @Override
    public void assignToBlock(MethodVertex rootMethod, LiteralVertex literal, int blockOrder) {
        findBlock(rootMethod, blockOrder).addEdge("AST", createTinkerGraphVertex(literal));
    }

    @Override
    public void assignToBlock(MethodVertex rootMethod, BlockVertex block, int blockOrder) {
        if (blockOrder == 0) {
            findVertex(rootMethod).addEdge(EdgeLabels.AST.toString(), createTinkerGraphVertex(block));
        } else {
            findBlock(rootMethod, blockOrder).addEdge(EdgeLabels.AST.toString(), createTinkerGraphVertex(block));
        }
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
        final Vertex v = this.graph.addVertex(label);
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
