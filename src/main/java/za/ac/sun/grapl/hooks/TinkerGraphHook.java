package za.ac.sun.grapl.hooks;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class TinkerGraphHook extends GremlinHook {

    private static Logger log = LogManager.getLogger();

    private final Graph graph;
    private final String dir;

    private TinkerGraphHook(TinkerGraphHookBuilder builder) {
        super(TinkerGraph.open(builder.conf));
        graph = super.graph;
        if (!builder.createNewGraph) graph.traversal().io(builder.graphDir).read().iterate();
        this.dir = builder.graphDir;
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
        private final String graphDir;
        private BaseConfiguration conf;

        public TinkerGraphHookBuilder(final String graphDir) {
            this.createNewGraph = true;
            String ext = graphDir.substring(graphDir.lastIndexOf('.') + 1).toLowerCase();
            conf = new BaseConfiguration();
            conf.setProperty("gremlin.graph", "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph");
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

        public TinkerGraphHookBuilder createNewGraph(final boolean createNewGraph) {
            this.createNewGraph = createNewGraph;
            return this;
        }

        public TinkerGraphHookBuilder conf(final BaseConfiguration conf) {
            this.conf = conf;
            return this;
        }

        public TinkerGraphHook build() {
            return new TinkerGraphHook(this);
        }
    }
}
