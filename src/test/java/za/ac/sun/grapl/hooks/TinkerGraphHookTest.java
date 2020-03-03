package za.ac.sun.grapl.hooks;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.jupiter.api.*;
import za.ac.sun.grapl.domain.enums.*;
import za.ac.sun.grapl.domain.models.GraPLEdge;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;
import za.ac.sun.grapl.hooks.TinkerGraphHook.TinkerGraphHookBuilder;

import java.io.File;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;


public class TinkerGraphHookTest {

    private static final String testGraphML = "/tmp/grapl/testGraph.xml";
    private static final String testGraphJSON = "/tmp/grapl/testGraph.json";
    private static final String testGryo = "/tmp/grapl/testGraph.kryo";

    @AfterAll
    static void tearDownAll() {
        File f = new File(testGraphML);
        if (f.exists()) f.delete();
        f = new File(testGraphJSON);
        if (f.exists()) f.delete();
        f = new File(testGryo);
        if (f.exists()) f.delete();
    }

    @Nested
    @DisplayName("TinkerGraph: Graph import file types")
    class ValidateGraphImportFileTypes {

        private TinkerGraphHook hook;

        @Test
        public void testImportingGraphML() {
            hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            hook.createVertex(new FileVertex("Test1", 0));
            hook.createVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();

            hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(false).build();
            // TODO: Once getVertex is coded, check if there are file vertices
        }

        @Test
        public void testImportingGraphJSON() {
            hook = new TinkerGraphHookBuilder(testGraphJSON).createNewGraph(true).build();
            hook.createVertex(new FileVertex("Test1", 0));
            hook.createVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();

            hook = new TinkerGraphHookBuilder(testGraphJSON).createNewGraph(false).build();
            // TODO: Once getVertex is coded, check if there are file vertices
        }

        @Test
        public void testImportingGryo() {
            hook = new TinkerGraphHookBuilder(testGryo).createNewGraph(true).build();
            hook.createVertex(new FileVertex("Test1", 0));
            hook.createVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();

            hook = new TinkerGraphHookBuilder(testGryo).createNewGraph(false).build();
            // TODO: Once getVertex is coded, check if there are file vertices
        }

        @Test
        public void testImportingInvalidExtension() {
            assertThrows(IllegalArgumentException.class, () -> new TinkerGraphHookBuilder("/tmp/grapl/invalid.txt").createNewGraph(true).build());
        }
    }

    @Nested
    @DisplayName("TinkerGraph: Graph export file types")
    class ValidateGraphExportFileTypes {

        private TinkerGraphHook hook;

        @BeforeEach
        public void setUp() {
            File f = new File(testGraphML);
            if (f.exists()) f.delete();
            f = new File(testGraphJSON);
            if (f.exists()) f.delete();
            f = new File(testGryo);
            if (f.exists()) f.delete();
        }

        @Test
        public void testExportingGraphML() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            hook.createVertex(new FileVertex("Test1", 0));
            hook.createVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();
            assertTrue(new File(testGraphML).exists());
        }

        @Test
        public void testExportingGraphJSON() {
            this.hook = new TinkerGraphHookBuilder(testGraphJSON).createNewGraph(true).build();
            hook.createVertex(new FileVertex("Test1", 0));
            hook.createVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();
            assertTrue(new File(testGraphJSON).exists());
        }

        @Test
        public void testExportingGryo() {
            this.hook = new TinkerGraphHookBuilder(testGryo).createNewGraph(true).build();
            hook.createVertex(new FileVertex("Test1", 0));
            hook.createVertex(new FileVertex("Test2", 1));
            hook.exportCurrentGraph();
            assertTrue(new File(testGryo).exists());
        }
    }

    @Nested
    @DisplayName("TinkerGraph: Graph export methods")
    class ValidateGraphExport {

        private TinkerGraphHook hook;

        @BeforeEach
        public void setUp() {
            File f = new File(testGraphML);
            if (f.exists()) f.delete();
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
        }

        @Test
        public void testInterfaceDefaults() {
            assertEquals("UNKNOWN", GraPLVertex.LABEL.toString());
            assertEquals(EnumSet.noneOf(VertexBaseTraits.class), GraPLVertex.TRAITS);
            assertEquals("AST", GraPLEdge.LABEL.toString());
        }

        @Test
        public void testTinkerGraphCanHandleAllVertexTypes() {
            hook.createVertex(new ArrayInitializerVertex());
            hook.createVertex(new BindingVertex("n", "s"));
            hook.createVertex(new BlockVertex("c", 0, 0, "t", 0));
            hook.createVertex(new CallVertex("c", "n", 0, "m", "m",
                    1, DispatchTypes.STATIC_DISPATCH, "s", "t", 0));
            hook.createVertex(new ControlStructureVertex("c", 0, 0, 0));
            hook.createVertex(new FieldIdentifier("c", "c", 0, 0, 0));
            hook.createVertex(new FileVertex("n", 1));
            hook.createVertex(new IdentifierVertex("c", "n", 0, 0, "t", 0));
            hook.createVertex(new LiteralVertex("c", 0, 0, "t", 0));
            hook.createVertex(new LocalVertex("c", "n", "t", 0, 0));
            hook.createVertex(new MemberVertex("c", "n", "t", 0));
            hook.createVertex(new MetaDataVertex("Java", "1.8"));
            hook.createVertex(new MethodParameterInVertex("c", "n", EvaluationStrategies.BY_REFERENCE, "t", 0, 0));
            hook.createVertex(new MethodRefVertex("c", 0, 0, "m", "m", 0));
            hook.createVertex(new MethodReturnVertex("c", "t", EvaluationStrategies.BY_REFERENCE, 0, 0));
            hook.createVertex(new MethodVertex("n", "f", "s", 0, 0));
            hook.createVertex(new ModifierVertex(ModifierTypes.PUBLIC, 0));
            hook.createVertex(new NamespaceBlockVertex("n", "f", 0));
            hook.createVertex(new ReturnVertex(0, 0, 0, "c"));
            hook.createVertex(new TypeArgumentVertex(0));
            hook.createVertex(new TypeDeclVertex("n", "f", "t"));
            hook.createVertex(new TypeParameterVertex("n", 0));
            hook.createVertex(new TypeVertex("n", "f", "t"));
            hook.createVertex(new UnknownVertex("c", 0, 0, 0, "t"));
            hook.exportCurrentGraph();
            assertTrue(new File(testGraphML).exists());
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
            this.m = new MethodVertex("test", "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.createVertex(m);
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
            this.f = new FileVertex("io.grapl.Test", 0);
            this.hook.createVertex(f);
        }

        @Test
        public void testJoinFile2NamespaceBlock() {
            NamespaceBlockVertex nbv = new NamespaceBlockVertex("grapl", "io.grapl", 0);
            this.hook.createVertex(nbv);
            this.hook.joinFileVertexTo(f, nbv);
            this.hook.exportCurrentGraph();

            GraphTraversalSource g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(FileVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(NamespaceBlockVertex.LABEL.toString(), "fullName", "io.grapl")
                    .hasNext());
        }

        @Test
        public void testJoinFile2Method() {
            MethodVertex mv = new MethodVertex("test", "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.createVertex(mv);
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
            this.m = new MethodVertex(ROOT_METHOD, "io.grapl.Test.run", "(I)", 0, 0);
            this.hook.createVertex(m);
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
    }
}
