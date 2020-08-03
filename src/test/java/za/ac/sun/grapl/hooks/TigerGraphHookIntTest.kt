package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.models.vertices.FileVertex
import za.ac.sun.grapl.domain.models.vertices.NamespaceBlockVertex
import java.io.File

class TigerGraphHookIntTest : HookTest() {
    override fun provideHook(): TigerGraphHook = provideBuilder().build()

    override fun provideBuilder(): TigerGraphHook.Builder = TigerGraphHook.Builder()

    @Test
    fun eeee() {
        val f = FileVertex("test", 3)
        val n = NamespaceBlockVertex("test", "test", 2)
        val hook = provideHook()
        hook.joinFileVertexTo(f, n)
        hook.updateASTVertexProperty(2, "name", "test1")
        println(hook.isASTVertex(2))
        println(hook.areASTVerticesJoinedByEdge(2, 3, EdgeLabels.AST))
//        hook?.createVertex(ArrayInitializerVertex(1))
//        hook?.createVertex(BindingVertex("test", "test"))
//        println(hook?.maxOrder())
    }

    @AfterEach
    fun tearDown() {
        provideHook().clearGraph()
    }
}