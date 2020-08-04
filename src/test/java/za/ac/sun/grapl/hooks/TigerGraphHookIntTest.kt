package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TigerGraphHookIntTest : HookTest() {

    override fun provideHook(): TigerGraphHook = provideBuilder().build()

    override fun provideBuilder(): TigerGraphHook.Builder = TigerGraphHook.Builder()

    @Nested
    inner class RestPPCheckMethodJoinInteraction : CheckMethodJoinInteraction() {
        lateinit var tigerGraphHook: TigerGraphHook

        @BeforeEach
        override fun setUp() {
            super.setUp()
            tigerGraphHook = hook as TigerGraphHook
        }

        @Test
        override fun joinMethodToMethodParamIn() {
            super.joinMethodToMethodParamIn()
        }

        @Test
        override fun joinMethodToMethodReturn() {
            super.joinMethodToMethodReturn()
        }

        @Test
        override fun joinMethodToModifier() {
            super.joinMethodToModifier()
        }
    }

    @Nested
    inner class RestPPFileJoinInteraction: FileJoinInteraction()

    @Nested
    inner class RestPPBlockJoinInteraction: BlockJoinInteraction()

    @Nested
    inner class RestPPNamespaceBlockJoinInteraction: NamespaceBlockJoinInteraction()

    @Nested
    inner class RestPPUpdateChecks: UpdateChecks()

    @Nested
    inner class RestPPAggregateQueries : AggregateQueries()

    @Nested
    internal inner class RestPPBooleanChecks : BooleanChecks()

    @Nested
    internal inner class RestPPASTManipulation : ASTManipulation()

//    @Test
//    fun eeee() {
//        val f = FileVertex("test", 3)
//        val n = NamespaceBlockVertex("test", "test", 2)
//        val hook = provideHook()
//        hook.joinFileVertexTo(f, n)
//        hook.updateASTVertexProperty(2, "name", "test1")
//        println(hook.isASTVertex(2))
//        println(hook.areASTVerticesJoinedByEdge(2, 3, EdgeLabels.AST))
//    }

//    @AfterEach
//    fun tearDown() {
//        provideHook().clearGraph()
//    }
}