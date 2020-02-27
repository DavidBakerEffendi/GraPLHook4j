package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;

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

    private static Graph importGraph(String dir) {
        TinkerGraph g = TinkerGraph.open();
        g.traversal().io(dir).read().iterate();
        return g;
    }

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

    @Override
    public void createVertex(GraPLVertex gv) {
        createTinkerGraphVertex(gv);
    }

    public void getVertex(GraPLVertex v) {
        // TODO: This should infer the key and be used when joining two vertices
        throw new NotImplementedException();
    }

    private HashSet<String> inferIdKeys(VertexLabels label) {
        final HashSet<String> keys = new HashSet<>();
        switch (label) {
            case META_DATA:
                keys.add("language");
                break;
            case FILE:
                keys.add("name");
                break;
            case METHOD:
                keys.add("fullName");
                keys.add("signature");
                break;
            case METHOD_PARAMETER_IN:
            case METHOD_RETURN:
                keys.add("typeFullName");
                break;
            case MODIFIER:
                keys.add("modifierType");
                break;
            case TYPE:
                break;
            case TYPE_DECL:
                break;
            case BINDING:
                break;
            case TYPE_PARAMETER:
                break;
            case TYPE_ARGUMENT:
                break;
            case MEMBER:
                break;
            case NAMESPACE_BLOCK:
                break;
            case LITERAL:
                break;
            case CALL:
                break;
            case LOCAL:
                break;
            case IDENTIFIER:
                break;
            case FIELD_IDENTIFIER:
                break;
            case RETURN:
                break;
            case BLOCK:
                break;
            case ARRAY_INITIALIZER:
                break;
            case METHOD_REF:
                break;
            case CONTROL_STRUCTURE:
                break;
            case UNKNOWN:
                break;
        }
        return keys;
    }

    @Override
    public boolean putVertexIfAbsent(GraPLVertex v, String key, Object value) {
        String label = "UNKNOWN";
        try {
            label = ((VertexLabels) v.getClass().getField("LABEL").get("LABEL")).name();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        final GraphTraversal<Vertex, Vertex> t = this.graph.traversal().V().hasLabel(label).has(key, P.eq(value));
        if (!t.hasNext()) {
            createVertex(v);
            return true;
        } else {
            return false;
        }
    }

    private Vertex findVertex(MethodVertex from) {
        GraphTraversalSource g = graph.traversal();
        return g.V().has(MethodVertex.LABEL.toString(), "fullName", from.fullName)
                .has("signature", from.signature).next();
    }

    private Vertex findVertex(FileVertex from) {
        GraphTraversalSource g = graph.traversal();
        return g.V().has(FileVertex.LABEL.toString(), "name", from.name).next();
    }

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
