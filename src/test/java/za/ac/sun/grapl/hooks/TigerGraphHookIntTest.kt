package za.ac.sun.grapl.hooks

import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.mappers.VertexMapper
import java.io.IOException

class TigerGraphHookIntTest : HookTest() {

    override fun provideHook(): TigerGraphHook = provideBuilder().build()

    override fun provideBuilder(): TigerGraphHook.Builder = TigerGraphHook.Builder()
            .hostname(DEFAULT_HOSTNAME).port(DEFAULT_PORT).secure(false)

    private fun headers(): Map<String, String> = mapOf("Content-Type" to "application/json")

    fun get(endpoint: String): JSONArray {
        val response = khttp.get(
                url = "http://$DEFAULT_HOSTNAME:$DEFAULT_PORT/$endpoint",
                headers = headers()
        )
        return response.jsonObject["results"] as JSONArray
    }

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
            val methodParamInRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.METHOD_PARAMETER_IN.name}_VERT")
            assertNotNull(methodParamInRaw.any())
            val methodParamIn = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(methodParamInRaw.first() as JSONObject))
            print(methodParamIn)
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

    companion object {
        private const val DEFAULT_HOSTNAME = "127.0.0.1"
        private const val DEFAULT_PORT = 9000
        private const val GRAPH_NAME = "cpg"
        private const val MAX_RETRY = 5
    }

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

}