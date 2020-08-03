package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.models.vertices.ArrayInitializerVertex
import za.ac.sun.grapl.domain.models.vertices.BindingVertex
import za.ac.sun.grapl.domain.models.vertices.FileVertex
import za.ac.sun.grapl.domain.models.vertices.NamespaceBlockVertex

class TigerGraphHookIntTest {

    @Test
    fun eeee() {
        val f = FileVertex("test", 3)
        val n = NamespaceBlockVertex("test", "test", 2)
        hook?.joinFileVertexTo(f, n)
        hook?.updateASTVertexProperty(2, "name", "test1")
        println(hook?.isASTVertex(2))
        println(hook?.areASTVerticesJoinedByEdge(2, 3, EdgeLabels.AST))
//        hook?.createVertex(ArrayInitializerVertex(1))
//        hook?.createVertex(BindingVertex("test", "test"))
//        println(hook?.maxOrder())
    }

    companion object {
        private var hook: TigerGraphHook? = null

        @BeforeAll
        @JvmStatic
        @Throws(Exception::class)
        internal fun setUpAll() {
            // Setup schema and build test database
            hook = TigerGraphHook.Builder().build()
        }

        @AfterAll
        @JvmStatic
        internal fun tearDownAll() {
//            hook?.clearGraph()
            hook?.close()
        }
    }

}