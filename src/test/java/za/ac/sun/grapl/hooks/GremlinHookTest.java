package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import za.ac.sun.grapl.domain.enums.*;
import za.ac.sun.grapl.domain.models.vertices.*;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

abstract public class GremlinHookTest {

    final static Logger logger = LogManager.getLogger();

    final static String tempDir = System.getProperty("java.io.tmpdir");
    final static String testGraphML = tempDir + "/grapl/graplhook4j_test.xml";
    final static String testGraphSON = tempDir + "/grapl/graplhook4j_test.json";
    final static String testGryo = tempDir + "/grapl/graplhook4j_test.kryo";

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
    public GremlinHook provideHook() {
        return new TinkerGraphHook.TinkerGraphHookBuilder().build();
    }

    /**
     * Provides a hook with the contents of a serialized graph to populate the graph with.
     * Default is a {@link TinkerGraphHook}.
     *
     * @param existingGraph the path to a GraphML, GraphSON, or Gryo graph.
     * @return a hook connected to a graph database populated with the contents of the file at the given path.
     */
    public GremlinHook provideHook(String existingGraph) {
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
    @DisplayName("Join method vertex to method related vertices")
    class MethodJoinInteraction {

        private GremlinHook hook;
        private MethodVertex m;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            FileVertex f = new FileVertex("Test", 0);
            this.m = new MethodVertex("run", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);
        }

        @AfterEach
        public void tearDown() {
            this.hook.clearGraph();
        }

        @Test
        public void testJoinMethod2MethodParamIn() {
            this.hook.createAndAddToMethod(m, new MethodParameterInVertex("test", "I", EvaluationStrategies.BY_VALUE, "I", 1, 1));

            this.hook.startTransaction();
            assertTrue(this.hook.g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(this.hook.g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodParameterInVertex.LABEL.toString(), "code", "test")
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testJoinMethod2MethodReturn() {
            this.hook.createAndAddToMethod(m, new MethodReturnVertex("INTEGER", "I", EvaluationStrategies.BY_VALUE, 0, 0));

            this.hook.startTransaction();
            assertTrue(this.hook.g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(this.hook.g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodReturnVertex.LABEL.toString(), "name", "INTEGER")
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testJoinMethod2Modifier() {
            this.hook.createAndAddToMethod(m, new ModifierVertex(ModifierTypes.PUBLIC, 0));

            this.hook.startTransaction();
            assertTrue(this.hook.g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(this.hook.g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(ModifierVertex.LABEL.toString(), "name", ModifierTypes.PUBLIC)
                    .hasNext());
            this.hook.endTransaction();
        }
    }

    @Nested
    @DisplayName("Join file vertex to file related vertices")
    class FileJoinInteraction {

        private GremlinHook hook;
        private FileVertex f;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.f = new FileVertex("Test", 1);
            this.hook.addFileVertex(f);
        }

        @AfterEach
        public void tearDown() {
            this.hook.clearGraph();
        }

        @Test
        public void testJoinFile2NamespaceBlock() {
            final NamespaceBlockVertex nbv = new NamespaceBlockVertex("grapl", "io.grapl", 0);
            this.hook.joinFileVertexTo(f, nbv);

            this.hook.startTransaction();
            assertTrue(this.hook.g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(this.hook.g.V().has(NamespaceBlockVertex.LABEL.toString(), "fullName", "io.grapl")
                    .out(EdgeLabels.AST.toString())
                    .hasLabel(FileVertex.LABEL.toString())
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testJoinFile2Method() {
            MethodVertex mv = new MethodVertex("test", "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.joinFileVertexTo(f, mv);

            this.hook.startTransaction();
            assertTrue(this.hook.g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(this.hook.g.V().hasLabel(FileVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodVertex.LABEL.toString(), "fullName", "io.grapl.Test.run")
                    .hasNext());
            this.hook.endTransaction();
        }
    }

    @Nested
    @DisplayName("Join file vertex to file related vertices")
    class BlockJoinInteraction {

        private static final String ROOT_METHOD = "root";
        private static final String FIRST_BLOCK = "firstBlock";
        private static final String TEST_ID = "test";
        private GremlinHook hook;
        private MethodVertex m;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            final FileVertex f = new FileVertex("Test", 0);
            this.m = new MethodVertex(ROOT_METHOD, "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.joinFileVertexTo(f, m);
            this.hook.createAndAssignToBlock(m, new BlockVertex(FIRST_BLOCK, 1, 1, "INTEGER", 5));
        }

        @AfterEach
        public void tearDown() {
            this.hook.clearGraph();
        }

        @Test
        public void testMethodJoinBlockTest() {
            this.hook.startTransaction();
            assertTrue(this.hook.g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(this.hook.g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testBlockJoinBlockTest() {
            final BlockVertex bv2 = new BlockVertex(TEST_ID, 2, 1, "INTEGER", 6);
            this.hook.createAndAssignToBlock(m, bv2, 1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(BlockVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testAssignLiteralToBlock() {
            final LiteralVertex lv = new LiteralVertex(TEST_ID, 2, 1, "INTEGER", 5);
            this.hook.createAndAssignToBlock(m, lv, 1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(LiteralVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testAssignLiteralToBlockSlow() {
            final LiteralVertex lv = new LiteralVertex(TEST_ID, 2, 1, "INTEGER", 5);
            this.hook.createAndAssignToBlock(lv, 1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(LiteralVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testAssignLocalToBlock() {
            final LocalVertex lv = new LocalVertex("1", TEST_ID, "INTEGER", 5, 2);
            this.hook.createAndAssignToBlock(m, lv, 1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(LocalVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testAssignLocalToBlockSlow() {
            final LocalVertex lv = new LocalVertex("1", TEST_ID, "INTEGER", 5, 2);
            this.hook.createAndAssignToBlock(lv, 1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(LocalVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testAssignControlToBlock() {
            final ControlStructureVertex lv = new ControlStructureVertex(TEST_ID, 2, 2, 1);
            this.hook.createAndAssignToBlock(m, lv, 1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(ControlStructureVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testAssignControlToBlockSlow() {
            final ControlStructureVertex lv = new ControlStructureVertex(TEST_ID, 2, 2, 1);
            this.hook.createAndAssignToBlock(lv, 1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(ControlStructureVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
            this.hook.endTransaction();
        }
    }

    @Nested
    @DisplayName("Join namespace block related vertices")
    class NamespaceBlockJoinInteraction {

        private static final String ROOT_NAME = "za";
        private static final String FIRST_BLOCK = "ac";
        private static final String SECOND_BLOCK = "sun";
        private GremlinHook hook;
        private NamespaceBlockVertex root;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.root = new NamespaceBlockVertex(ROOT_NAME, ROOT_NAME, 0);
        }

        @AfterEach
        public void tearDown() {
            this.hook.clearGraph();
        }

        @Test
        public void joinTwoNamespaceBlocks() {
            final NamespaceBlockVertex n1 = new NamespaceBlockVertex(FIRST_BLOCK, ROOT_NAME.concat(".").concat(FIRST_BLOCK), 1);
            this.hook.joinNamespaceBlocks(root, n1);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", ROOT_NAME)
                    .out(EdgeLabels.AST.toString())
                    .has(NamespaceBlockVertex.LABEL.toString(), "name", FIRST_BLOCK).hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void joinThreeNamespaceBlocks() {
            final NamespaceBlockVertex n1 = new NamespaceBlockVertex(FIRST_BLOCK, ROOT_NAME.concat(".").concat(FIRST_BLOCK), 1);
            final NamespaceBlockVertex n2 = new NamespaceBlockVertex(SECOND_BLOCK, ROOT_NAME.concat(".").concat(SECOND_BLOCK), 1);
            this.hook.joinNamespaceBlocks(root, n1);
            this.hook.joinNamespaceBlocks(n1, n2);

            this.hook.startTransaction();
            assertTrue(this.hook.g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", ROOT_NAME)
                    .out(EdgeLabels.AST.toString())
                    .has(NamespaceBlockVertex.LABEL.toString(), "name", FIRST_BLOCK).hasNext());
            assertTrue(this.hook.g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(NamespaceBlockVertex.LABEL.toString(), "name", SECOND_BLOCK).hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void joinExistingConnection() {
            final NamespaceBlockVertex n1 = new NamespaceBlockVertex(FIRST_BLOCK, ROOT_NAME.concat(".").concat(FIRST_BLOCK), 1);
            this.hook.joinNamespaceBlocks(root, n1);
            this.hook.joinNamespaceBlocks(root, n1);

            this.hook.startTransaction();
            assertEquals(1, this.hook.g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", ROOT_NAME)
                    .out(EdgeLabels.AST.toString()).count().next());
            this.hook.endTransaction();
        }
    }

    @Nested
    @DisplayName("Aggregate queries")
    class GremlinAggregateQueries {

        private GremlinHook hook;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
        }

        @AfterEach
        public void tearDown() {
            this.hook.clearGraph();
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
            this.hook.createAndAssignToBlock(m, new BlockVertex("firstBlock", 1, 1, "INTEGER", 5));
            this.hook.createAndAssignToBlock(m, new BlockVertex("secondBlock", 2, 1, "INTEGER", 6));
            assertEquals(2, this.hook.maxOrder());
        }

        @Test
        public void testNonEmptyGraphWithoutASTVertices() {
            assertEquals(0, this.hook.maxOrder());
        }
    }

    @Nested
    @DisplayName("Simple boolean checks")
    class GremlinBooleanChecks {

        private GremlinHook hook;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
        }

        @AfterEach
        public void tearDown() {
            this.hook.clearGraph();
        }

        @Test
        public void testEmptyGraph() {
            assertFalse(this.hook.isASTVertex(0));
        }

        @Test
        public void testNonEmptyGraphWithBlockVertices() {
            final FileVertex f = new FileVertex("Test", 0);
            final MethodVertex m = new MethodVertex("root", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);
            this.hook.createAndAssignToBlock(m, new BlockVertex("firstBlock", 2, 1, "INTEGER", 5));
            this.hook.createAndAssignToBlock(m, new BlockVertex("secondBlock", 3, 1, "INTEGER", 6));
            assertTrue(this.hook.isASTVertex(0));
            assertTrue(this.hook.isASTVertex(1));
            assertTrue(this.hook.isASTVertex(2));
            assertTrue(this.hook.isASTVertex(3));
        }

        @Test
        public void testNonEmptyGraphWithoutBlockVertices() {
            final FileVertex f = new FileVertex("Test", 0);
            final MethodVertex m = new MethodVertex("root", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);

            assertTrue(this.hook.isASTVertex(0));
            assertTrue(this.hook.isASTVertex(1));
        }
    }

    @Nested
    @DisplayName("Upsert Checks")
    class GremlinUpdateChecks {

        private GremlinHook hook;

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
        }

        @AfterEach
        public void tearDown() {
            this.hook.clearGraph();
        }

        @Test
        public void testUpdateOnOneBlockPropertyFromParent() {
            final String keyToTest = "typeFullName";
            final String initValue = "INTEGER";
            final String updatedValue = "VOID";
            assertNotEquals(initValue, updatedValue);

            final FileVertex f = new FileVertex("Test", 0);
            final MethodVertex m = new MethodVertex("root", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);
            this.hook.createAndAssignToBlock(m, new BlockVertex("firstBlock", 2, 1, initValue, 5));
            this.hook.startTransaction();
            assertTrue(this.hook.g.V().hasLabel(BlockVertex.LABEL.toString()).has(keyToTest, initValue).hasNext());
            this.hook.endTransaction();
            this.hook.updateASTVertexProperty(m, 2, keyToTest, updatedValue);
            this.hook.startTransaction();
            assertTrue(this.hook.g.V().hasLabel(BlockVertex.LABEL.toString()).has(keyToTest, updatedValue).hasNext());
            this.hook.endTransaction();
        }

        @Test
        public void testUpdateOnOneBlockPropertyWithoutParent() {
            final String keyToTest = "typeFullName";
            final String initValue = "INTEGER";
            final String updatedValue = "VOID";
            assertNotEquals(initValue, updatedValue);

            this.hook.createVertex(new BlockVertex("firstBlock", 1, 1, initValue, 5));
            this.hook.startTransaction();
            assertTrue(this.hook.g.V().hasLabel(BlockVertex.LABEL.toString()).has(keyToTest, initValue).hasNext());
            this.hook.endTransaction();
            this.hook.updateASTVertexProperty(1, keyToTest, updatedValue);
            this.hook.startTransaction();
            assertTrue(this.hook.g.V().hasLabel(BlockVertex.LABEL.toString()).has(keyToTest, updatedValue).hasNext());
            this.hook.endTransaction();
        }

    }

    @Nested()
    @DisplayName("AST vertex unstructured manipulation queries")
    class GremlinASTManipulation {

        private IHook hook;
        private final int TEST_ID_1 = 1;
        private final int TEST_ID_2 = 2;
        private final BlockVertex bv1 = new BlockVertex("test1", TEST_ID_1, 1, "INTEGER", 6);
        private final BlockVertex bv2 = new BlockVertex("test2", TEST_ID_2, 1, "INTEGER", 7);

        @BeforeEach
        public void setUp() {
            this.hook = provideHook();
            this.hook.clearGraph();
        }

        @Test
        public void testCreatingFreeBlock() {
            assertFalse(this.hook.isASTVertex(TEST_ID_1));
            this.hook.createVertex(bv1);
            assertTrue(this.hook.isASTVertex(TEST_ID_1));
        }

        @Test
        public void testCreatingAndJoiningFreeBlocks() {
            this.hook.createVertex(bv1);
            this.hook.createVertex(bv2);
            assertFalse(this.hook.areASTVerticesJoinedByEdge(TEST_ID_1, TEST_ID_2, EdgeLabels.AST));
            assertFalse(this.hook.areASTVerticesJoinedByEdge(TEST_ID_2, TEST_ID_1, EdgeLabels.AST));
            this.hook.joinASTVerticesByOrder(TEST_ID_1, TEST_ID_2, EdgeLabels.AST);
            assertTrue(this.hook.areASTVerticesJoinedByEdge(TEST_ID_1, TEST_ID_2, EdgeLabels.AST));
            assertTrue(this.hook.areASTVerticesJoinedByEdge(TEST_ID_2, TEST_ID_1, EdgeLabels.AST));
        }
    }
}
