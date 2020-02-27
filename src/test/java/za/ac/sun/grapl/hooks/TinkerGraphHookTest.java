package za.ac.sun.grapl.hooks;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.jupiter.api.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import za.ac.sun.grapl.domain.enums.*;
import za.ac.sun.grapl.domain.models.GraPLVertex;
import za.ac.sun.grapl.domain.models.vertices.*;
import za.ac.sun.grapl.hooks.TinkerGraphHook.TinkerGraphHookBuilder;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;


public class TinkerGraphHookTest {

    private static final String testGraphML = "/tmp/grapl/testGraph.xml";
    private static final String testGraphJSON = "/tmp/grapl/testGraph.json";
    private static final String testGryo = "/tmp/grapl/testGraph.kryo";

    @AfterAll
    static void tearDownAll() {
//        File f = new File(testGraphML);
//        if (f.exists()) f.delete();
//        f = new File(testGraphJSON);
//        if (f.exists()) f.delete();
//        f = new File(testGryo);
//        if (f.exists()) f.delete();
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
            hook.createVertex(new MethodReturnVertex("c", EvaluationStrategies.BY_REFERENCE, "t", 0, 0));
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
    @DisplayName("TinkerGraph: Graph interaction methods")
    class ValidateGraphInteraction {

        private TinkerGraphHook hook;
        private TinkerGraph testGraph;

        @BeforeEach
        public void setUp() {
            this.hook = new TinkerGraphHookBuilder(testGraphML).createNewGraph(true).build();
            this.testGraph = TinkerGraph.open();
        }

        @Test
        public void testJoinMethod2MethodParamIn() {
            MethodVertex m = new MethodVertex("test", "io.grapl.test.Main.run", "(I)", 0, 0);
            this.hook.createVertex(m);
            this.hook.createAndAddToMethod(m, new MethodParameterInVertex("test", "I", EvaluationStrategies.BY_VALUE, "I", 1, 1));
            this.hook.exportCurrentGraph();

            GraphTraversalSource  g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodParameterInVertex.LABEL.toString(), "code", "test")
                    .hasNext());
        }

        @Test
        public void testJoinMethod2MethodReturn() {
            MethodVertex m = new MethodVertex("test", "io.grapl.test.Main.run", "(I)V", 0, 0);
            this.hook.createVertex(m);
            this.hook.createAndAddToMethod(m, new MethodReturnVertex("(I)V", EvaluationStrategies.BY_VALUE, "I", 0, 0));
            this.hook.exportCurrentGraph();

            GraphTraversalSource  g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(MethodReturnVertex.LABEL.toString(), "code", "(I)V")
                    .hasNext());
        }

        @Test
        public void testJoinMethod2Modifier() {
            MethodVertex m = new MethodVertex("test", "io.grapl.test.Main.run", "(I)V", 0, 0);
            this.hook.createVertex(m);
            this.hook.createAndAddToMethod(m, new ModifierVertex(ModifierTypes.PUBLIC, 0));
            this.hook.exportCurrentGraph();

            GraphTraversalSource  g = testGraph.traversal();
            g.io(testGraphML).read().iterate();

            assertTrue(g.E().hasLabel(EdgeLabels.AST.toString()).hasNext());
            assertTrue(g.V().hasLabel(MethodVertex.LABEL.toString())
                    .out(EdgeLabels.AST.toString())
                    .has(ModifierVertex.LABEL.toString(), "modifierType", ModifierTypes.PUBLIC.toString())
                    .hasNext());
        }

        @Test
        public void testPutIfVertexIfAbsentPositive() {
            GraPLVertex v = new ModifierVertex(ModifierTypes.PUBLIC, 0);
            assertTrue(this.hook.putVertexIfAbsent(v, "modifierType", ModifierTypes.PUBLIC.toString()));
        }

        @Test
        public void testPutIfVertexIfAbsentNegative() {
            GraPLVertex v = new ModifierVertex(ModifierTypes.PUBLIC, 0);
            this.hook.createVertex(v);
            assertFalse(this.hook.putVertexIfAbsent(v, "modifierType", ModifierTypes.PUBLIC.toString()));
        }

    }

}
