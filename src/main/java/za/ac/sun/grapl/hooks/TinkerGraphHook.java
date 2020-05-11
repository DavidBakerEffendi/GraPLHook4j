package za.ac.sun.grapl.hooks;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.File;

public class TinkerGraphHook extends GremlinHook {

    private TinkerGraphHook(TinkerGraphHookBuilder builder) {
        super(TinkerGraph.open(builder.conf));
        if (builder.graphDir != null) super.graph.traversal().io(builder.graphDir).read().iterate();
    }

    public static class TinkerGraphHookBuilder implements IHookBuilder {
        private String graphDir;
        private BaseConfiguration conf;

        public TinkerGraphHookBuilder() {
            this.graphDir = null;
            this.conf = new BaseConfiguration();
            this.conf.setProperty("gremlin.graph", "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph");
        }

        public TinkerGraphHookBuilder useExistingGraph(final String graphDir) {
            if (!isValidExportPath(graphDir)) {
                throw new IllegalArgumentException("Unsupported graph extension! Supported types are GraphML," +
                        " GraphSON, and Gryo.");
            }
            if (!new File(graphDir).exists()) {
                throw new IllegalArgumentException("No existing serialized graph file was found at " + graphDir);
            }
            this.graphDir = graphDir;
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
