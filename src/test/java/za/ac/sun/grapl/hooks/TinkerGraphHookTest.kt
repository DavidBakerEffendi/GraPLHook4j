package za.ac.sun.grapl.hooks

import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import org.junit.jupiter.api.*
import za.ac.sun.grapl.domain.models.vertices.FileVertex
import java.io.File

class TinkerGraphHookTest : GremlinHookTest() {
    @Nested
    @DisplayName("Graph import file types")
    internal inner class ValidateGraphImportFileTypes {
        private var testGraph: Graph? = null

        @BeforeEach
        fun setUp() {
            testGraph = TinkerGraph.open()
        }

        @Test
        fun testImportingGraphML() {
            val hook = provideHook()
            hook.addFileVertex(FileVertex("Test1", 0))
            hook.addFileVertex(FileVertex("Test2", 1))
            hook.exportCurrentGraph(testGraphML)
            Assertions.assertDoesNotThrow<GremlinHook> { provideHook(testGraphML) }
            val g = testGraph!!.traversal()
            g.io<Any>(testGraphML).read().iterate()
            Assertions.assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext())
            Assertions.assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext())
        }

        @Test
        fun testImportingGraphJSON() {
            val hook = provideHook()
            hook.addFileVertex(FileVertex("Test1", 0))
            hook.addFileVertex(FileVertex("Test2", 1))
            hook.exportCurrentGraph(testGraphSON)
            Assertions.assertDoesNotThrow<GremlinHook> { provideHook(testGraphSON) }
            val g = testGraph!!.traversal()
            g.io<Any>(testGraphSON).read().iterate()
            Assertions.assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext())
            Assertions.assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext())
        }

        @Test
        fun testImportingGryo() {
            val hook = provideHook()
            hook.addFileVertex(FileVertex("Test1", 0))
            hook.addFileVertex(FileVertex("Test2", 1))
            hook.exportCurrentGraph(testGryo)
            Assertions.assertDoesNotThrow<GremlinHook> { provideHook(testGryo) }
            val g = testGraph!!.traversal()
            g.io<Any>(testGryo).read().iterate()
            Assertions.assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test1").hasNext())
            Assertions.assertTrue(g.V().has(FileVertex.LABEL.toString(), "name", "Test2").hasNext())
        }

        @Test
        fun testImportingGraphThatDNE() {
            Assertions.assertThrows(IllegalArgumentException::class.java) { provideBuilder().useExistingGraph("/tmp/grapl/DNE.kryo").build() }
        }

        @Test
        fun testImportingInvalidExtension() {
            Assertions.assertThrows(IllegalArgumentException::class.java) { provideBuilder().useExistingGraph("/tmp/grapl/invalid.txt").build() }
        }
    }

    @Nested
    @DisplayName("Graph export file types")
    internal inner class ValidateGraphExportFileTypes {

        @Test
        fun testExportingGraphML() {
            val hook = provideHook()
            hook.addFileVertex(FileVertex("Test1", 0))
            hook.addFileVertex(FileVertex("Test2", 1))
            hook.exportCurrentGraph(testGraphML)
            Assertions.assertTrue(File(testGraphML).exists())
        }

        @Test
        fun testExportingGraphJSON() {
            val hook = provideHook()
            hook.addFileVertex(FileVertex("Test1", 0))
            hook.addFileVertex(FileVertex("Test2", 1))
            hook.exportCurrentGraph(testGraphSON)
            Assertions.assertTrue(File(testGraphSON).exists())
        }

        @Test
        fun testExportingGryo() {
            val hook = provideHook()
            hook.addFileVertex(FileVertex("Test1", 0))
            hook.addFileVertex(FileVertex("Test2", 1))
            hook.exportCurrentGraph(testGryo)
            Assertions.assertTrue(File(testGryo).exists())
        }

        @Test
        fun testExportingInvalidFileType() {
            val hook = provideHook()
            hook.addFileVertex(FileVertex("Test1", 0))
            hook.addFileVertex(FileVertex("Test2", 1))
            Assertions.assertThrows(IllegalArgumentException::class.java) { hook.exportCurrentGraph("/tmp/grapl/invalid.txt") }
        }
    }
}