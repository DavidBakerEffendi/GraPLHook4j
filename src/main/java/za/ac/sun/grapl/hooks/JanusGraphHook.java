package za.ac.sun.grapl.hooks;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.JanusGraphManagement;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class JanusGraphHook extends GremlinHook {

    private static final Logger log = LogManager.getLogger();

    private final JanusGraph graph;
    private final boolean supportsTransactions;
    private JanusGraphTransaction tx;

    private JanusGraphHook(JanusGraphHookBuilder builder) {
        super(JanusGraphFactory.open(builder.conf));
        this.graph = (JanusGraph) super.graph;
        if (builder.graphDir != null) super.graph.traversal().io(builder.graphDir).read().iterate();
        this.supportsTransactions = graph.features().graph().supportsTransactions();
    }

    @Override
    protected void startTransaction() {
        if (supportsTransactions) {
            log.debug("Supports tx");
            if (tx == null || tx.isClosed()) {
                log.debug("Created new tx");
                tx = graph.newTransaction();
            }
            super.setTraversalSource(tx.traversal());
        } else {
            super.setTraversalSource(graph.traversal());
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
                    if (tx.isClosed()) return;
                    tx.commit();
                    success = true;
                } catch (JanusGraphException | IllegalStateException e) {
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

    public void clearGraph() {
        startTransaction();
        this.tx.traversal().V().drop().iterate();
        endTransaction();
    }

    public void close() {
        this.graph.close();
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
                throw new UnsupportedOperationException("Export to Kryo and JSON currently not supported using the JanusGraphHook.");
            case ("xml"):
                tx.traversal().io(exportDir).write().iterate();
                break;

        }
        endTransaction();
    }

    public static class JanusGraphHookBuilder {
        private String graphDir;
        private boolean createNewGraph;
        private String conf;

        public JanusGraphHookBuilder() {
            this.createNewGraph = true;
            BaseConfiguration conf = new BaseConfiguration();
            conf.setProperty("gremlin.graph", "org.janusgraph.core.JanusGraphFactory");
            conf.setProperty("storage.backend", "inmemory");
        }

        public JanusGraphHookBuilder clearDatabase(final boolean createNewGraph) {
            this.createNewGraph = createNewGraph;
            return this;
        }

        public JanusGraphHook.JanusGraphHookBuilder useExistingGraph(final String graphDir) {
            if (!isValidExportPath(graphDir)) {
                throw new IllegalArgumentException("Unsupported graph extension! Supported types are GraphML," +
                        " GraphSON, and Gryo.");
            } else if (!new File(graphDir).exists()) {
                throw new IllegalArgumentException("No existing serialized graph file was found at " + graphDir);
            }
            this.graphDir = graphDir;
            return this;
        }

        public JanusGraphHookBuilder conf(final String conf) {
            this.conf = conf;
            return this;
        }

        public JanusGraphHook build() throws Exception {
            if (this.createNewGraph) clearAndSetSchema();
            return new JanusGraphHook(this);
        }

        private void clearAndSetSchema() throws Exception {
            log.info("Dropping existing graph data.");
            JanusGraphFactory.drop(JanusGraphFactory.open(this.conf));
            log.info("Creating CPG schema.");
            final JanusGraph graph = JanusGraphFactory.open(this.conf);
            final JanusGraphManagement mgmt = graph.openManagement();
            Arrays.stream(VertexLabels.values()).forEach(label -> mgmt.makeVertexLabel(label.toString()).make());
            Arrays.stream(EdgeLabels.values()).forEach(label -> mgmt.makeVertexLabel(label.toString()).make());
            // Collect all possible properties
            HashSet<String> graphProperties = new HashSet<>();
            Arrays.stream(ArrayInitializerVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(BindingVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(BlockVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(CallVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(ControlStructureVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(FieldIdentifier.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(FileVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(IdentifierVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(LiteralVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(LocalVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(MemberVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(MetaDataVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(MethodParameterInVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(MethodRefVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(MethodReturnVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(MethodVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(ModifierVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(NamespaceBlockVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(ReturnVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(TypeArgumentVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(TypeDeclVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(TypeParameterVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(TypeVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            Arrays.stream(UnknownVertex.class.getFields()).forEach(e -> graphProperties.add(e.getName()));
            // Add all unique properties to the schema
            graphProperties.parallelStream()
                    .filter((field -> !"LABEL".equals(field) && !"TRAITS".equals(field)))
                    .forEach(field -> mgmt.makePropertyKey(field)
                            .dataType(String.class).cardinality(Cardinality.SINGLE).make());
            // Create indexes
            mgmt.buildIndex("byASTOrder", Vertex.class)
                    .addKey(mgmt.getPropertyKey("order"))
                    .buildCompositeIndex();
            mgmt.buildIndex("byFullName", Vertex.class)
                    .addKey(mgmt.getPropertyKey("fullName"))
                    .buildCompositeIndex();
            // Commit and close for now
            mgmt.commit();
            JanusGraphFactory.close(graph);
        }
    }

}
