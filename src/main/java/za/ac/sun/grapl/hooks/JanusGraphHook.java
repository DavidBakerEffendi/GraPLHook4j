package za.ac.sun.grapl.hooks;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.diskstorage.BackendException;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.enums.VertexLabels;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.util.Arrays;
import java.util.HashSet;

public class JanusGraphHook extends GremlinHook {

    private static Logger log = LogManager.getLogger();

    private final JanusGraph graph;
    private final boolean supportsTransactions;
    private JanusGraphTransaction tx;

    private JanusGraphHook(JanusGraphHookBuilder builder) throws BackendException {
        super(JanusGraphFactory.open(builder.conf));
        this.graph = (JanusGraph) super.graph;
        this.supportsTransactions = graph.features().graph().supportsTransactions();
        if (builder.createNewGraph) clearAndSetSchema();
    }

    @Override
    protected void startTransaction() {
        if (supportsTransactions) {
            tx = graph.newTransaction();
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

    @Override
    protected Graph getGraph() {
        return this.graph;
    }

    private void clearAndSetSchema() throws BackendException {
        log.info("Dropping existing graph data.");
//        JanusGraphFactory.drop(graph);
        startTransaction();
        tx.traversal().V().drop().iterate();
        endTransaction();
        log.info("Creating CPG schema.");
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
        mgmt.commit();
    }

    public static class JanusGraphHookBuilder {
        private boolean createNewGraph;
        private BaseConfiguration conf;

        public JanusGraphHookBuilder() {
            this.createNewGraph = true;
            conf = new BaseConfiguration();
            conf.setProperty("gremlin.graph", "org.janusgraph.core.JanusGraphFactory");
            conf.setProperty("storage.backend", "inmemory");
        }

        public JanusGraphHookBuilder createNewGraph(final boolean createNewGraph) {
            this.createNewGraph = createNewGraph;
            return this;
        }

        public JanusGraphHookBuilder conf(final BaseConfiguration conf) {
            this.conf = conf;
            return this;
        }

        public JanusGraphHook build() throws BackendException {
            return new JanusGraphHook(this);
        }
    }

}
