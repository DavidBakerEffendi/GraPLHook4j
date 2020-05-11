package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.jupiter.api.*;
import za.ac.sun.grapl.domain.enums.*;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GremlinHookTest {

    final static Logger logger = LogManager.getLogger();

    static final String tempDir = System.getProperty("java.io.tmpdir");
    static final String testGraphML = tempDir + "/grapl/graplhook4j_test.xml";
    static final String testGraphSON = tempDir + "/grapl/graplhook4j_test.json";
    static final String testGryo = tempDir + "/grapl/graplhook4j_test.kryo";

    private Class<?> getTestClass() {
        return this.getClass();
    }

    @AfterAll
    static void tearDownAll() {
        File[] testFiles = new File[]{new File(testGraphML), new File(testGraphSON), new File(testGryo)};
        Arrays.stream(testFiles).forEach(file -> {
            try {
                if (!file.delete())
                    logger.warn("Could not clear test resources.");
            } catch (Exception e) {
                logger.warn("Could not clear test resources.", e);
            }
        });
    }

    /**
     * Provides a hook to a new database hook. Default is a new {@link TinkerGraphHook}.
     *
     * @return a built hook.
     */
    public IHook provideHook() {
        return new TinkerGraphHook.TinkerGraphHookBuilder().build();
    }

    /**
     * Provides a hook with the contents of a serialized graph to populate the graph with.
     * Default is a {@link TinkerGraphHook}.
     *
     * @param existingGraph the path to a GraphML, GraphSON, or Gryo graph.
     * @return a hook connected to a graph database populated with the contents of the file at the given path.
     */
    public IHook provideHook(String existingGraph) {
        return new TinkerGraphHook.TinkerGraphHookBuilder().useExistingGraph(existingGraph).build();
    }

    /**
     * Provides a hook builder to continue to configure.
     * Default is a {@link za.ac.sun.grapl.hooks.TinkerGraphHook.TinkerGraphHookBuilder}.
     *
     * @return an {@link IHookBuilder} to build with.
     */
    public IHookBuilder provideBuilder() {
        return new TinkerGraphHook.TinkerGraphHookBuilder();
    }

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

            if (getTestClass().equals(JanusGraphHookIntTest.class)) {
                assertThrows(UnsupportedOperationException.class, () -> hook.exportCurrentGraph(testGraphSON));
            } else {
                hook.exportCurrentGraph(testGraphSON);

                assertDoesNotThrow(() -> provideHook(testGraphSON));

                GraphTraversalSource g = testGraph.traversal();
                g.io(testGraphSON).read().iterate();
                assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
                assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
            }
        }

        @Test
        public void testImportingGryo() {
            hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));

            if (getTestClass().equals(JanusGraphHookIntTest.class)) {
                assertThrows(UnsupportedOperationException.class, () -> hook.exportCurrentGraph(testGraphSON));
            } else {
                hook.exportCurrentGraph(testGryo);

                assertDoesNotThrow(() -> provideHook(testGryo));

                GraphTraversalSource g = testGraph.traversal();
                g.io(testGryo).read().iterate();
                assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
                assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
            }
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

        private IHook hook;

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
            if (getTestClass().equals(JanusGraphHookIntTest.class)) {
                assertThrows(UnsupportedOperationException.class, () -> hook.exportCurrentGraph(testGraphSON));
            } else {
                hook.exportCurrentGraph(testGraphSON);
                assertTrue(new File(testGraphSON).exists());
            }
        }

        @Test
        public void testExportingGryo() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            if (getTestClass().equals(JanusGraphHookIntTest.class)) {
                assertThrows(UnsupportedOperationException.class, () -> hook.exportCurrentGraph(testGryo));
            } else {
                hook.exportCurrentGraph(testGryo);
                assertTrue(new File(testGryo).exists());
            }
        }

        @Test
        public void testExportingInvalidFileType() {
            this.hook = provideHook();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            assertThrows(IllegalArgumentException.class, () -> hook.exportCurrentGraph("/tmp/grapl/invalid.txt"));
        }
    }

    @Nested
    @DisplayName("Graph export methods")
    class ValidateGraphDomain {

        @Test
        public void testInterfaceDefaults() {
            assertEquals("UNKNOWN", GraPLVertex.LABEL.toString());
            assertEquals(EnumSet.noneOf(VertexBaseTraits.class), GraPLVertex.TRAITS);
        }

        @Test
        public void testTinkerGraphCreateAllVertexTypes() {
            new ArrayInitializerVertex();
            new BindingVertex("n", "s");
            new BlockVertex("c", 0, 0, "t", 0);
            new CallVertex("c", "n", 0, "m", "m",
                    1, DispatchTypes.STATIC_DISPATCH, "s", "t", 0);
            new ControlStructureVertex("c", 0, 0, 0);
            new FieldIdentifier("c", "c", 0, 0, 0);
            new FileVertex("n", 1);
            new IdentifierVertex("c", "n", 0, 0, "t", 0);
            new LiteralVertex("c", 0, 0, "t", 0);
            new LocalVertex("c", "n", "t", 0, 0);
            new MemberVertex("c", "n", "t", 0);
            new MetaDataVertex("Java", "1.8");
            new MethodParameterInVertex("c", "n", EvaluationStrategies.BY_REFERENCE, "t", 0, 0);
            new MethodRefVertex("c", 0, 0, "m", "m", 0);
            new MethodReturnVertex("c", "t", EvaluationStrategies.BY_REFERENCE, 0, 0);
            new MethodVertex("n", "f", "s", 0, 0);
            new ModifierVertex(ModifierTypes.PUBLIC, 0);
            new NamespaceBlockVertex("n", "f", 0);
            new ReturnVertex(0, 0, 0, "c");
            new TypeArgumentVertex(0);
            new TypeDeclVertex("n", "f", "t");
            new TypeParameterVertex("n", 0);
            new TypeVertex("n", "f", "t");
            new UnknownVertex("c", 0, 0, 0, "t");
        }

    }

    @Nested
    @DisplayName("Join method vertex to method related vertices")
    class MethodJoinInteraction {

        private IHook hook;
        private TinkerGraph testGraph;
        private MethodVertex m;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.testGraph = TinkerGraph.open();
            FileVertex f = new FileVertex("Test", 0);
            this.m = new MethodVertex("run", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);
        }

        @Test
        public void testJoinMethod2MethodParamIn() {
            this.hook.createAndAddToMethod(m, new MethodParameterInVertex("test", "I", EvaluationStrategies.BY_VALUE, "I", 1, 1));
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodParameterInVertex.LABEL.toString(), "code", "test")
                    .hasNext());
        }

        @Test
        public void testJoinMethod2MethodReturn() {
            this.hook.createAndAddToMethod(m, new MethodReturnVertex("INTEGER", "I", EvaluationStrategies.BY_VALUE, 0, 0));
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodReturnVertex.LABEL.toString(), "name", "INTEGER")
                    .hasNext());
        }

        @Test
        public void testJoinMethod2Modifier() {
            this.hook.createAndAddToMethod(m, new ModifierVertex(ModifierTypes.PUBLIC, 0));
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(ModifierVertex.LABEL.toString(), "name", ModifierTypes.PUBLIC.toString())
                    .hasNext());
        }
    }

    @Nested
    @DisplayName("Join file vertex to file related vertices")
    class FileJoinInteraction {

        private IHook hook;
        private TinkerGraph testGraph;
        private FileVertex f;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.testGraph = TinkerGraph.open();
            this.f = new FileVertex("Test", 1);
            this.hook.addFileVertex(f);
        }

        @Test
        public void testJoinFile2NamespaceBlock() {
            NamespaceBlockVertex nbv = new NamespaceBlockVertex("grapl", "io.grapl", 0);
            this.hook.joinFileVertexTo(f, nbv);
            this.hook.exportCurrentGraph(testGraphML);
            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().has(NamespaceBlockVertex.LABEL.toString(), "fullName", "io.grapl")
                    .out(EdgeLabels.AST.toString())
                    .hasLabel(FileVertex.LABEL.toString())
                    .hasNext());
        }

        @Test
        public void testJoinFile2Method() {
            MethodVertex mv = new MethodVertex("test", "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.joinFileVertexTo(f, mv);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(FileVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodVertex.LABEL.toString(), "fullName", "io.grapl.Test.run")
                    .hasNext());
        }
    }

    @Nested
    @DisplayName("Join file vertex to file related vertices")
    class BlockJoinInteraction {

        private static final String ROOT_METHOD = "root";
        private static final String FIRST_BLOCK = "firstBlock";
        private static final String TEST_ID = "test";
        private IHook hook;
        private TinkerGraph testGraph;
        private MethodVertex m;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.testGraph = TinkerGraph.open();
            FileVertex f = new FileVertex("Test", 0);
            this.m = new MethodVertex(ROOT_METHOD, "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.joinFileVertexTo(f, m);
            this.hook.assignToBlock(m, new BlockVertex(FIRST_BLOCK, 1, 1, "INTEGER", 5), 0);
        }

        @Test
        public void testMethodJoinBlockTest() {
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();
            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .hasNext());
        }

        @Test
        public void testBlockJoinBlockTest() {
            BlockVertex bv2 = new BlockVertex(TEST_ID, 2, 1, "INTEGER", 6);
            this.hook.assignToBlock(m, bv2, 1);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(BlockVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
        }

        @Test
        public void testAssignLiteralToBlock() {
            LiteralVertex lv = new LiteralVertex(TEST_ID, 2, 1, "INTEGER", 5);
            this.hook.assignToBlock(m, lv, 1);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(LiteralVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
        }

        @Test
        public void testAssignLocalToBlock() {
            LocalVertex lv = new LocalVertex("1", TEST_ID, "INTEGER", 5, 2);
            this.hook.assignToBlock(m, lv, 1);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(LocalVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
        }

        @Test
        public void testAssignControlToBlock() {
            ControlStructureVertex lv = new ControlStructureVertex(TEST_ID, 2, 2, 1);
            this.hook.assignToBlock(m, lv, 1);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(ControlStructureVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
        }
    }

    @Nested
    @DisplayName("Join namespace block related vertices")
    class NamespaceBlockJoinInteraction {

        private static final String ROOT_NAME = "za";
        private static final String FIRST_BLOCK = "ac";
        private static final String SECOND_BLOCK = "sun";
        private IHook hook;
        private TinkerGraph testGraph;
        private NamespaceBlockVertex root;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.testGraph = TinkerGraph.open();
            this.root = new NamespaceBlockVertex(ROOT_NAME, ROOT_NAME, 0);
        }

        @Test
        public void joinTwoNamespaceBlocks() {
            NamespaceBlockVertex n1 = new NamespaceBlockVertex(FIRST_BLOCK, ROOT_NAME.concat(".").concat(FIRST_BLOCK), 1);
            this.hook.joinNamespaceBlocks(root, n1);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", ROOT_NAME)
                    .out(EdgeLabels.AST.toString())
                    .has(NamespaceBlockVertex.LABEL.toString(), "name", FIRST_BLOCK).hasNext());
        }

        @Test
        public void joinThreeNamespaceBlocks() {
            NamespaceBlockVertex n1 = new NamespaceBlockVertex(FIRST_BLOCK, ROOT_NAME.concat(".").concat(FIRST_BLOCK), 1);
            NamespaceBlockVertex n2 = new NamespaceBlockVertex(SECOND_BLOCK, ROOT_NAME.concat(".").concat(SECOND_BLOCK), 1);
            this.hook.joinNamespaceBlocks(root, n1);
            this.hook.joinNamespaceBlocks(n1, n2);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", ROOT_NAME)
                    .out(EdgeLabels.AST.toString())
                    .has(NamespaceBlockVertex.LABEL.toString(), "name", FIRST_BLOCK).hasNext());
            assertTrue(g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(NamespaceBlockVertex.LABEL.toString(), "name", SECOND_BLOCK).hasNext());
        }

        @Test
        public void joinExistingConnection() {
            NamespaceBlockVertex n1 = new NamespaceBlockVertex(FIRST_BLOCK, ROOT_NAME.concat(".").concat(FIRST_BLOCK), 1);
            this.hook.joinNamespaceBlocks(root, n1);
            this.hook.joinNamespaceBlocks(root, n1);
            this.hook.exportCurrentGraph(testGraphML);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertEquals(1, g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", ROOT_NAME)
                    .out(EdgeLabels.AST.toString()).count().next());
        }
    }

    @Nested
    @DisplayName("Aggregate queries")
    class GremlinAggregateQueries {

        private IHook hook;
        private TinkerGraph testGraph;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.testGraph = TinkerGraph.open();
            if (getTestClass().equals(JanusGraphHookIntTest.class)) {
                ((JanusGraphHook) this.hook).clearGraph();
            }
        }

        @Test
        public void testEmptyGraph() {
            assertEquals(0, this.hook.maxOrder());
        }

        @Test
        public void testNonEmptyGraphWithASTVertices() {
            FileVertex f = new FileVertex("Test", 0);
            MethodVertex m = new MethodVertex("root", "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.joinFileVertexTo(f, m);
            this.hook.assignToBlock(m, new BlockVertex("firstBlock", 1, 1, "INTEGER", 5), 0);
            this.hook.assignToBlock(m, new BlockVertex("secondBlock", 2, 1, "INTEGER", 6), 0);
            assertEquals(2, this.hook.maxOrder());
        }

        @Test
        public void testNonEmptyGraphWithoutASTVertices() {
            testGraph.addVertex("EmptyVertex");
            testGraph.traversal().io(testGraphML).write().iterate();
            this.hook = provideHook(testGraphML);
            assertEquals(0, this.hook.maxOrder());
        }
    }

    @Nested
    @DisplayName("Simple boolean checks")
    class GremlinBooleanChecks {

        private IHook hook;
        private TinkerGraph testGraph;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.testGraph = TinkerGraph.open();
        }

        @Test
        public void testEmptyGraph() {
            assertFalse(this.hook.isBlock(0));
        }

        @Test
        public void testNonEmptyGraphWithBlockVertices() {
            FileVertex f = new FileVertex("Test", 0);
            MethodVertex m = new MethodVertex("root", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);
            this.hook.assignToBlock(m, new BlockVertex("firstBlock", 2, 1, "INTEGER", 5), 0);
            this.hook.assignToBlock(m, new BlockVertex("secondBlock", 3, 1, "INTEGER", 6), 0);
            assertFalse(this.hook.isBlock(0));
            assertFalse(this.hook.isBlock(1));
            assertTrue(this.hook.isBlock(2));
            assertTrue(this.hook.isBlock(3));
        }

        @Test
        public void testNonEmptyGraphWithoutBlockVertices() {
            testGraph.addVertex("EmptyVertex");
            FileVertex f = new FileVertex("Test", 0);
            MethodVertex m = new MethodVertex("root", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);
            testGraph.traversal().io(testGraphML).write().iterate();
            this.hook = provideHook(testGraphML);

            assertFalse(this.hook.isBlock(0));
            assertFalse(this.hook.isBlock(1));
        }
    }
}
