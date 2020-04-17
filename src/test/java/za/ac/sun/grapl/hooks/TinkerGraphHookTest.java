package za.ac.sun.grapl.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.jupiter.api.*;
import za.ac.sun.grapl.domain.enums.*;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;
import za.ac.sun.grapl.hooks.TinkerGraphHook.TinkerGraphHookBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;


public class TinkerGraphHookTest {

    final static Logger logger = LogManager.getLogger();

    private static final String testGraphML = "/tmp/grapl/graplhook4j_test.xml";
    private static final String testGraphSON = "/tmp/grapl/graplhook4j_test.json";
    private static final String testGryo = "/tmp/grapl/graplhook4j_test.kryo";

    @AfterAll
    static void tearDownAll() {
        File[] testFiles = new File[]{new File(testGraphML), new File(testGraphSON), new File(testGryo)};
        Arrays.stream(testFiles).forEach(file -> {
            try {
                if (!file.delete())
                    logger.warn("Could not clear " + TinkerGraphHookTest.class.getName() + "'s test resources.");
            } catch (Exception e) {
                logger.warn("Could not clear " + TinkerGraphHookTest.class.getName() + "'s test resources.", e);
            }
        });
    }

    @Nested
    @DisplayName("TinkerGraph: Graph import file types")
    class ValidateGraphImportFileTypes {

        private TinkerGraphHook hook;
        private TinkerGraph testGraph;

        @BeforeEach
        public void setUp() {
            this.testGraph = TinkerGraph.open();
        }

        @Test
        public void testImportingGraphML() {
            hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();

            assertDoesNotThrow(new TinkerGraphHookBuilder(testGraphML).createNewGraph(false)::build);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
        }

        @Test
        public void testImportingGraphJSON() {
            hook = new TinkerGraphHookBuilder(testGraphSON).createNewGraph(true).build();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();

            assertDoesNotThrow(new TinkerGraphHookBuilder(testGraphSON).createNewGraph(false)::build);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphSON).read().iterate();
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
        }

        @Test
        public void testImportingGryo() {
            hook = new TinkerGraphHookBuilder(testGryo).createNewGraph(true).build();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();

            assertDoesNotThrow(new TinkerGraphHookBuilder(testGryo).createNewGraph(false)::build);

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGryo).read().iterate();
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext());
            assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext());
        }

        @Test
        public void testImportingGraphThatDNE() {
            assertThrows(IllegalStateException.class,
                    () -> new TinkerGraphHookBuilder("/tmp/grapl/DNE.kryo").createNewGraph(false).build());
        }

        @Test
        public void testImportingInvalidExtension() {
            assertThrows(IllegalArgumentException.class,
                    () -> new TinkerGraphHookBuilder("/tmp/grapl/invalid.txt").createNewGraph(true).build());
        }
    }

    @Nested
    @DisplayName("TinkerGraph: Graph export file types")
    class ValidateGraphExportFileTypes {

        private TinkerGraphHook hook;

        @Test
        public void testExportingGraphML() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();
            assertTrue(new File(testGraphML).exists());
        }

        @Test
        public void testExportingGraphJSON() {
            this.hook = new TinkerGraphHookBuilder(testGraphSON).createNewGraph(true).build();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();
            assertTrue(new File(testGraphSON).exists());
        }

        @Test
        public void testExportingGryo() {
            this.hook = new TinkerGraphHookBuilder(testGryo).createNewGraph(true).build();
            hook.addFileVertex(new FileVertex("Test1", 0));
            hook.addFileVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();
            assertTrue(new File(testGryo).exists());
        }
    }

    @Nested
    @DisplayName("TinkerGraph: Graph export methods")
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
    @DisplayName("TinkerGraph: Join method vertex to method related vertices")
    class MethodJoinInteraction {

        private TinkerGraphHook hook;
        private TinkerGraph testGraph;
        private MethodVertex m;

        @BeforeEach
        public void setUp() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            this.testGraph = TinkerGraph.open();
            FileVertex f = new FileVertex("Test", 0);
            this.m = new MethodVertex("run", "io.grapl.Test.run", "(I)", 0, 1);
            this.hook.joinFileVertexTo(f, m);
        }

        @Test
        public void testJoinMethod2MethodParamIn() {
            this.hook.createAndAddToMethod(m, new MethodParameterInVertex("test", "I", EvaluationStrategies.BY_VALUE, "I", 1, 1));
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

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
    @DisplayName("TinkerGraph: Join file vertex to file related vertices")
    class FileJoinInteraction {

        private TinkerGraphHook hook;
        private TinkerGraph testGraph;
        private FileVertex f;

        @BeforeEach
        public void setUp() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            this.testGraph = TinkerGraph.open();
            this.f = new FileVertex("Test", 1);
            this.hook.addFileVertex(f);
        }

        @Test
        public void testJoinFile2NamespaceBlock() {
            NamespaceBlockVertex nbv = new NamespaceBlockVertex("grapl", "io.grapl", 0);
            this.hook.joinFileVertexTo(f, nbv);
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

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
    @DisplayName("TinkerGraph: Join file vertex to file related vertices")
    class BlockJoinInteraction {

        private static final String ROOT_METHOD = "root";
        private static final String FIRST_BLOCK = "firstBlock";
        private static final String TEST_ID = "test";
        private TinkerGraphHook hook;
        private TinkerGraph testGraph;
        private MethodVertex m;

        @BeforeEach
        public void setUp() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            this.testGraph = TinkerGraph.open();
            FileVertex f = new FileVertex("Test", 0);
            this.m = new MethodVertex(ROOT_METHOD, "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.joinFileVertexTo(f, m);
            this.hook.assignToBlock(m, new BlockVertex(FIRST_BLOCK, 1, 1, "INTEGER", 5), 0);
        }

        @Test
        public void testMethodJoinBlockTest() {
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.V().has(BlockVertex.LABEL.toString(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.toString())
                    .has(ControlStructureVertex.LABEL.toString(), "name", TEST_ID)
                    .hasNext());
        }
    }

    @Nested
    @DisplayName("TinkerGraph: Join namespace block related vertices")
    class NamespaceBlockJoinInteraction {

        private static final String ROOT_NAME = "za";
        private static final String FIRST_BLOCK = "ac";
        private static final String SECOND_BLOCK = "sun";
        private TinkerGraphHook hook;
        private TinkerGraph testGraph;
        private NamespaceBlockVertex root;

        @BeforeEach
        public void setUp() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            this.testGraph = TinkerGraph.open();
            this.root = new NamespaceBlockVertex(ROOT_NAME, ROOT_NAME, 0);
        }

        @Test
        public void joinTwoNamespaceBlocks() {
            NamespaceBlockVertex n1 = new NamespaceBlockVertex(FIRST_BLOCK, ROOT_NAME.concat(".").concat(FIRST_BLOCK), 1);
            this.hook.joinNamespaceBlocks(root, n1);
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

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
            this.hook.exportCurrentGraph();

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertEquals(1, g.V().has(NamespaceBlockVertex.LABEL.toString(), "name", ROOT_NAME)
                    .out(EdgeLabels.AST.toString()).count().next());
        }
    }

    @Nested
    @DisplayName("TinkerGraph: Aggregate queries")
    class TinkerGraphAggregateQueries {

        private TinkerGraphHook hook;
        private TinkerGraph testGraph;

        @BeforeEach
        public void setUp() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            this.testGraph = TinkerGraph.open();
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
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(false).build();

            assertEquals(0, this.hook.maxOrder());
        }
    }

    @Nested
    @DisplayName("TinkerGraph: Simple boolean checks")
    class TinkerGraphBooleanChecks {

        private TinkerGraphHook hook;
        private TinkerGraph testGraph;

        @BeforeEach
        public void setUp() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
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
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(false).build();

            assertFalse(this.hook.isBlock(0));
            assertFalse(this.hook.isBlock(1));
        }
    }
}
