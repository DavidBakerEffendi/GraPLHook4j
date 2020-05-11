package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

public class JanusGraphHookIntTest extends GremlinHookTest {

    private static final Logger log = LogManager.getLogger();

    @BeforeAll
    static void setUpAll() {
        // Setup schema and build test database
        JanusGraphHook hook = new JanusGraphHook.JanusGraphHookBuilder()
                .conf("src/test/resources/conf/janusgraph-config.properties")
                .setSchema(true)
                .clearDatabase(true)
                .build();
        hook.clearGraph();
        hook.close();
    }

    @Override
    public IHookBuilder provideBuilder() {
        return new JanusGraphHook.JanusGraphHookBuilder().
                conf("src/test/resources/conf/janusgraph-config.properties").clearDatabase(true);
    }

    @Override
    public IHook provideHook() {
        try {
            return new JanusGraphHook.JanusGraphHookBuilder()
                    .conf("src/test/resources/conf/janusgraph-config.properties").clearDatabase(true).build();
        } catch (Exception e) {
            log.error("Unable to build JanusGraphHook!", e);
            System.exit(1);
            return null;
        }
    }

    @Override
    public IHook provideHook(String existingGraph) {
        try {
            return new JanusGraphHook
                    .JanusGraphHookBuilder()
                    .conf("src/test/resources/conf/janusgraph-config.properties")
                    .useExistingGraph(existingGraph)
                    .clearDatabase(true)
                    .build();
        } catch (Exception e) {
            log.error("Unable to build JanusGraphHook!", e);
            System.exit(1);
            return null;
        }
    }

}
