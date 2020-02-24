package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.GraPLVertex;

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

    private static Graph importGraph(String dir) {
        TinkerGraph g = TinkerGraph.open();
        g.traversal().io(dir).read().iterate();
        return g;
    }

    @Override
    public void createVertex(GraPLVertex gv) {
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
                v.property(f.getName(), f.get(gv));
            } catch (IllegalAccessException e) {
                log.error("Illegal field access when adding properties to '" + gv.LABEL.name() + "'.", e);
            }
        });

    }

    @Override
    public void getVertex(Object identifier, VertexLabels label) {
        throw new NotImplementedException();
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
