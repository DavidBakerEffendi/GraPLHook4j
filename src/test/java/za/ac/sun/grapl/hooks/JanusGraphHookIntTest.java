package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class JanusGraphHookIntTest extends GremlinHookTest {

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

}
