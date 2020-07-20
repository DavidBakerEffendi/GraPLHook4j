package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;

import java.io.File;

public final class JanusGraphHook extends GremlinHook {

    private static final Logger log = LogManager.getLogger();

    private final Graph graph;
    private final boolean supportsTransactions;
    private final String conf;
    private Transaction tx;

    private JanusGraphHook(JanusGraphHookBuilder builder) {
        super(builder.graph);
        this.conf = builder.conf;
        this.graph = super.graph;
        if (builder.graphDir != null) super.graph.traversal().io(builder.graphDir).read().iterate();
        this.supportsTransactions = graph.features().graph().supportsTransactions();
        try {
            if (builder.clearGraph) clearGraph();
        } catch (Exception e) {
            log.warn("Unable to clear graph!", e);
        }
    }

    @Override
    protected void startTransaction() {
        if (supportsTransactions) {
            log.debug("Supports tx");
            if (tx == null || !tx.isOpen()) {
                log.debug("Created new tx");
                try {
                    tx = AnonymousTraversalSource.traversal().withRemote(conf).tx();
                } catch (Exception e) {
                    log.error("Unable to create transaction!");
                }
            }
        } else {
            log.debug("Does not support tx");
        }
        try {
            super.setTraversalSource(AnonymousTraversalSource.traversal().withRemote(conf));
        } catch (Exception e) {
            log.error("Unable to create transaction!");
        }
    }

    @Override
    protected void endTransaction() {
        if (supportsTransactions) {
            boolean success = false;
            int failures = 0;
            final int waitTime = 5000;
            do {
                try {
                    if (tx == null) return;
                    if (!tx.isOpen()) return;
                    tx.commit();
                    success = true;
                } catch (IllegalStateException e) {
                    failures++;
                    if (failures > 3) {
                        log.error("Failed to commit transaction " + failures + " time(s). Aborting...");
                        return;
                    } else {
                        log.warn("Failed to commit transaction " + failures + " time(s). Backing off and retrying...");
                        try {
                            Thread.sleep(waitTime);
                        } catch (Exception ignored) {
                        }
                    }
                }
            } while (!success);
        } else {
            super.endTransaction();
        }
    }

    @Override
    public void exportCurrentGraph(String exportDir) {
        if (!isValidExportPath(exportDir)) {
            throw new IllegalArgumentException("Unsupported graph extension! Supported types are GraphML," +
                    " GraphSON, and Gryo.");
        }
        final String ext = exportDir.substring(exportDir.lastIndexOf('.') + 1).toLowerCase();
        startTransaction();
        switch (ext) {
            case ("kryo"):
            case ("json"):
            case ("xml"):
                throw new UnsupportedOperationException("Export to Kryo, XML, and JSON currently not supported using the JanusGraphHook.");
        }
        endTransaction();
    }

    public static class JanusGraphHookBuilder implements IHookBuilder {
        private String graphDir;
        private boolean clearGraph;
        private final String conf;
        private Graph graph;

        public JanusGraphHookBuilder(final String pathToConf) {
            if (pathToConf == null) throw new AssertionError("Config path may not be null! See " +
                    "https://docs.janusgraph.org/connecting/java/ for how to configure JanusGraph remote connections.");
            this.conf = pathToConf;
        }

        public JanusGraphHookBuilder clearDatabase(final boolean clearDatabase) {
            this.clearGraph = clearDatabase;
            return this;
        }

        public JanusGraphHookBuilder useExistingGraph(final String graphDir) {
            if (!isValidExportPath(graphDir)) {
                throw new IllegalArgumentException("Unsupported graph extension! Supported types are GraphML," +
                        " GraphSON, and Gryo.");
            } else if (!new File(graphDir).exists()) {
                throw new IllegalArgumentException("No existing serialized graph file was found at " + graphDir);
            }
            this.graphDir = graphDir;
            return this;
        }

        public JanusGraphHook build() throws Exception {
            graph = AnonymousTraversalSource.traversal().withRemote(conf).getGraph();
            return new JanusGraphHook(this);
        }

    }

}
