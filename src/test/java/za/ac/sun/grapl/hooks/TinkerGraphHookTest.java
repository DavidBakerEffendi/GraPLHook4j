package za.ac.sun.grapl.hooks;

import org.apache.commons.configuration.BaseConfiguration;


public class TinkerGraphHookTest extends GremlinHookTest {

    @Override
    public BaseConfiguration provideConfig() {
        final BaseConfiguration conf = new BaseConfiguration();
        conf.setProperty("gremlin.graph", "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph");
        return conf;
    }

}
