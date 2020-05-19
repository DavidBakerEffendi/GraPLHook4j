package za.ac.sun.grapl.hooks;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import za.ac.sun.grapl.domain.models.vertices.FileVertex;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public final class TinkerGraphHookTest extends GremlinHookTest {

    @Nested
    @DisplayName("Graph import file types")
    class ValidateGraphImportFileTypes {

        private IHook hook;
        private Graph testGraph;

        @BeforeEach
        public void setUp() {
            this.testGraph = TinkerGraph.open();
        }

        @Test
        public void testImportingGraphML() {
            hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph(testGraphML);

            assertDoesNotThrow(() -> provideHook(testGraphML));

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
        }

        @Test
        public void testImportingGraphJSON() {
            hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));

            hook.exportCurrentGraph(testGraphSON);

            assertDoesNotThrow(() -> provideHook(testGraphSON));
            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphSON).read().iterate();
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
        }

        @Test
        public void testImportingGryo() {
            hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));

            hook.exportCurrentGraph(testGryo);

            assertDoesNotThrow(() -> provideHook(testGryo));
            GraphTraversalSource g = testGraph.traversal();
            g.io(testGryo).read().iterate();
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
        }

        @Test
        public void testImportingGraphThatDNE() {
            assertThrows(IllegalArgumentException.class, () -> provideBuilder().useExistingGraph("/tmp/grapl/DNE.kryo").build());
        }

        @Test
        public void testImportingInvalidExtension() {
            assertThrows(IllegalArgumentException.class, () -> provideBuilder().useExistingGraph("/tmp/grapl/invalid.txt").build());
        }
    }

    @Nested
    @DisplayName("Graph export file types")
    class ValidateGraphExportFileTypes {

        private GremlinHook hook;

        @Test
        public void testExportingGraphML() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph(testGraphML);
            assertTrue(new File(testGraphML).exists());
        }

        @Test
        public void testExportingGraphJSON() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph(testGraphSON);
            assertTrue(new File(testGraphSON).exists());
        }

        @Test
        public void testExportingGryo() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph(testGryo);
            assertTrue(new File(testGryo).exists());
        }

        @Test
        public void testExportingInvalidFileType() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            assertThrows(IllegalArgumentException.class, () -> hook.exportCurrentGraph("/tmp/grapl/invalid.txt"));
        }
    }

}
