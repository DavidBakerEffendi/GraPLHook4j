package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import za.ac.sun.grapl.domain.models.vertices.FileVertex;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JanusGraphHookIntTest extends GremlinHookTest {

    private static final Logger log = LogManager.getLogger();
    private static JanusGraphHook hook;

    @BeforeAll
    static void setUpAll() throws Exception {
        // Setup schema and build test database
        hook = new JanusGraphHook.JanusGraphHookBuilder()
                .conf("src/test/resources/conf/remote.properties")
                .clearDatabase(true)
                .build();
    }

    @AfterAll
    static void tearDownAll() {
        hook.clearGraph();
        hook.close();
    }

    @Override
    public IHookBuilder provideBuilder() {
        return new JanusGraphHook.JanusGraphHookBuilder().conf("src/test/resources/conf/remote.properties");
    }

    @Override
    public GremlinHook provideHook() {
        try {
            return new JanusGraphHook.JanusGraphHookBuilder().conf("src/test/resources/conf/remote.properties").build();
        } catch (Exception e) {
            log.error("Unable to build JanusGraphHook!", e);
            return null;
        }
    }

    @Override
    public GremlinHook provideHook(String existingGraph) {
        try {
            return new JanusGraphHook
                    .JanusGraphHookBuilder()
                    .conf("src/test/resources/conf/remote.properties")
                    .build();
        } catch (Exception e) {
            log.error("Unable to build JanusGraphHook!", e);
            return null;
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
            assertThrows(UnsupportedOperationException.class, () -> hook.exportCurrentGraph(testGraphML));
        }

        @Test
        public void testExportingGraphJSON() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            assertThrows(UnsupportedOperationException.class, () -> hook.exportCurrentGraph(testGraphSON));
        }

        @Test
        public void testExportingGryo() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            assertThrows(UnsupportedOperationException.class, () -> hook.exportCurrentGraph(testGryo));
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
