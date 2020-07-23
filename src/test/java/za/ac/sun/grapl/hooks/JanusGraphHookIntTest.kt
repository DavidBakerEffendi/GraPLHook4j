package za.ac.sun.grapl.hooks

import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.*
import za.ac.sun.grapl.domain.models.vertices.FileVertex

class JanusGraphHookIntTest : GremlinHookTest() {

    private val logger = LogManager.getLogger(JanusGraphHookIntTest::class.java)

    override fun provideBuilder(): GremlinHookBuilder {
        return JanusGraphHook.Builder("src/test/resources/conf/remote.properties")
    }

    override fun provideHook(): GremlinHook? {
        return try {
            JanusGraphHook.Builder("src/test/resources/conf/remote.properties").build()
        } catch (e: Exception) {
            logger.error("Unable to build JanusGraphHook!", e)
            null
        }
    }

    override fun provideHook(existingGraph: String): GremlinHook? {
        return try {
            JanusGraphHook.Builder("src/test/resources/conf/remote.properties")
                    .build()
        } catch (e: Exception) {
            logger.error("Unable to build JanusGraphHook!", e)
            null
        }
    }

    @Nested
    @DisplayName("Graph export file types")
    internal inner class ValidateGraphExportFileTypes {

        @Test
        fun testExportingGraphML() {
            val hook = provideHook()
            hook?.addFileVertex(FileVertex("Test1", 0))
            hook?.addFileVertex(FileVertex("Test2", 1))
            Assertions.assertThrows(UnsupportedOperationException::class.java) { hook?.exportCurrentGraph(testGraphML) }
        }

        @Test
        fun testExportingGraphJSON() {
            val hook = provideHook()
            hook?.addFileVertex(FileVertex("Test1", 0))
            hook?.addFileVertex(FileVertex("Test2", 1))
            Assertions.assertThrows(UnsupportedOperationException::class.java) { hook?.exportCurrentGraph(testGraphSON) }
        }

        @Test
        fun testExportingGryo() {
            val hook = provideHook()
            hook?.addFileVertex(FileVertex("Test1", 0))
            hook?.addFileVertex(FileVertex("Test2", 1))
            Assertions.assertThrows(UnsupportedOperationException::class.java) { hook?.exportCurrentGraph(testGryo) }
        }

        @Test
        fun testExportingInvalidFileType() {
            val hook = provideHook()
            hook?.addFileVertex(FileVertex("Test1", 0))
            hook?.addFileVertex(FileVertex("Test2", 1))
            Assertions.assertThrows(IllegalArgumentException::class.java) { hook?.exportCurrentGraph("/tmp/grapl/invalid.txt") }
        }
    }

    companion object {
        private var hook: JanusGraphHook? = null

        @BeforeAll
        @JvmStatic
        @Throws(Exception::class)
        internal fun setUpAll() {
            // Setup schema and build test database
            hook = JanusGraphHook.Builder("src/test/resources/conf/remote.properties")
                    .clearDatabase(true)
                    .build()
        }

        @AfterAll
        @JvmStatic
        internal fun tearDownAll() {
            hook?.clearGraph()
            hook?.close()
        }
    }
}