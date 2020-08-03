package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.models.vertices.FileVertex
import za.ac.sun.grapl.domain.models.vertices.NamespaceBlockVertex

class TigerGraphHookIntTest : HookTest() {

    override fun provideBuilder(): IHookBuilder = TigerGraphHook.Builder()

    @Nested
    inner class TigerGraphCheckMethodJoinInteraction : CheckMethodJoinInteraction() {

        override fun joinMethodToMethodParamIn() {
            super.joinMethodToMethodParamIn()
            // TODO: Assert
        }

    }

    @Test
    fun eeee() {
        val f = FileVertex("test", 3)
        val n = NamespaceBlockVertex("test", "test", 2)
        val hook = provideHook()!!
        hook.joinFileVertexTo(f, n)
        hook.updateASTVertexProperty(2, "name", "test1")
        println(hook.isASTVertex(2))
        println(hook.areASTVerticesJoinedByEdge(2, 3, EdgeLabels.AST))
//        hook?.createVertex(ArrayInitializerVertex(1))
//        hook?.createVertex(BindingVertex("test", "test"))
//        println(hook?.maxOrder())
    }

}