package za.ac.sun.grapl.hooks

import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.mappers.VertexMapper
import za.ac.sun.grapl.domain.models.vertices.*

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
            val methodRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.METHOD.name}_VERT")
            val fileRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.FILE.name}_VERT")
            assertNotNull(methodParamInRaw.any())
            assertNotNull(methodRaw.any())
            assertNotNull(fileRaw.any())
            val methodParamIn = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(methodParamInRaw.first() as JSONObject))
            val method = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(methodRaw.first() as JSONObject))
            val file = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(fileRaw.first() as JSONObject))
            assertTrue(methodParamIn is MethodParameterInVertex)
            assertTrue(method is MethodVertex)
            assertTrue(file is FileVertex)
            val methodEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.METHOD.name}_VERT/${method.hashCode()}")
            val fileEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.FILE.name}_VERT/${file.hashCode()}")
            assertNotNull(methodEdges.any())
            assertNotNull(fileEdges.any())
            assertEquals(method.hashCode().toString(), (methodEdges.first() as JSONObject)["from_id"])
            assertEquals(methodParamIn.hashCode().toString(), (methodEdges.first() as JSONObject)["to_id"])
            assertEquals(file.hashCode().toString(), (fileEdges.first() as JSONObject)["from_id"])
            assertEquals(method.hashCode().toString(), (fileEdges.first() as JSONObject)["to_id"])
        }

        @Test
        override fun joinMethodToMethodReturn() {
            super.joinMethodToMethodReturn()
            val methodReturnRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.METHOD_RETURN.name}_VERT")
            val methodRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.METHOD.name}_VERT")
            val fileRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.FILE.name}_VERT")
            assertNotNull(methodReturnRaw.any())
            assertNotNull(methodRaw.any())
            assertNotNull(fileRaw.any())
            val methodReturn = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(methodReturnRaw.first() as JSONObject))
            val method = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(methodRaw.first() as JSONObject))
            val file = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(fileRaw.first() as JSONObject))
            assertTrue(methodReturn is MethodReturnVertex)
            assertTrue(method is MethodVertex)
            assertTrue(file is FileVertex)
            val methodEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.METHOD.name}_VERT/${method.hashCode()}")
            val fileEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.FILE.name}_VERT/${file.hashCode()}")
            assertNotNull(methodEdges.any())
            assertNotNull(fileEdges.any())
            assertEquals(method.hashCode().toString(), (methodEdges.first() as JSONObject)["from_id"])
            assertEquals(methodReturn.hashCode().toString(), (methodEdges.first() as JSONObject)["to_id"])
            assertEquals(file.hashCode().toString(), (fileEdges.first() as JSONObject)["from_id"])
            assertEquals(method.hashCode().toString(), (fileEdges.first() as JSONObject)["to_id"])
        }

        @Test
        override fun joinMethodToModifier() {
            super.joinMethodToModifier()
            val modifierRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.MODIFIER.name}_VERT")
            val methodRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.METHOD.name}_VERT")
            val fileRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.FILE.name}_VERT")
            assertNotNull(modifierRaw.any())
            assertNotNull(methodRaw.any())
            assertNotNull(fileRaw.any())
            val modifier = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(modifierRaw.first() as JSONObject))
            val method = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(methodRaw.first() as JSONObject))
            val file = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(fileRaw.first() as JSONObject))
            assertTrue(modifier is ModifierVertex)
            assertTrue(method is MethodVertex)
            assertTrue(file is FileVertex)
            val methodEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.METHOD.name}_VERT/${method.hashCode()}")
            val fileEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.FILE.name}_VERT/${file.hashCode()}")
            assertNotNull(methodEdges.any())
            assertNotNull(fileEdges.any())
            assertEquals(method.hashCode().toString(), (methodEdges.first() as JSONObject)["from_id"])
            assertEquals(modifier.hashCode().toString(), (methodEdges.first() as JSONObject)["to_id"])
            assertEquals(file.hashCode().toString(), (fileEdges.first() as JSONObject)["from_id"])
            assertEquals(method.hashCode().toString(), (fileEdges.first() as JSONObject)["to_id"])
        }
    }

    @Nested
    inner class RestPPFileJoinInteraction : FileJoinInteraction() {
        lateinit var tigerGraphHook: TigerGraphHook

        @BeforeEach
        override fun setUp() {
            super.setUp()
            tigerGraphHook = hook as TigerGraphHook
        }

        @Test
        override fun testJoinFile2NamespaceBlock() {
            super.testJoinFile2NamespaceBlock()
            val namespaceRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.NAMESPACE_BLOCK.name}_VERT")
            val fileRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.FILE.name}_VERT")
            assertNotNull(namespaceRaw.any())
            assertNotNull(fileRaw.any())
            val namespace = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(namespaceRaw.first() as JSONObject))
            val file = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(fileRaw.first() as JSONObject))
            assertTrue(namespace is NamespaceBlockVertex)
            assertTrue(file is FileVertex)
            val namespaceEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.NAMESPACE_BLOCK.name}_VERT/${namespace.hashCode()}")
            assertNotNull(namespaceEdges.any())
            assertEquals(namespace.hashCode().toString(), (namespaceEdges.first() as JSONObject)["from_id"])
            assertEquals(file.hashCode().toString(), (namespaceEdges.first() as JSONObject)["to_id"])
        }

        @Test
        override fun testJoinFile2Method() {
            super.testJoinFile2Method()
            val methodRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.METHOD.name}_VERT")
            val fileRaw = get("graph/$GRAPH_NAME/vertices/${VertexLabels.FILE.name}_VERT")
            assertNotNull(methodRaw.any())
            assertNotNull(fileRaw.any())
            val method = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(methodRaw.first() as JSONObject))
            val file = VertexMapper.mapToVertex(tigerGraphHook.flattenVertexResult(fileRaw.first() as JSONObject))
            assertTrue(method is MethodVertex)
            assertTrue(file is FileVertex)
            val fileEdges = get("graph/$GRAPH_NAME/edges/${VertexLabels.FILE.name}_VERT/${file.hashCode()}")
            assertNotNull(fileEdges.any())
            assertEquals(file.hashCode().toString(), (fileEdges.first() as JSONObject)["from_id"])
            assertEquals(method.hashCode().toString(), (fileEdges.first() as JSONObject)["to_id"])
        }
    }

    @Nested
    inner class RestPPBlockJoinInteraction : BlockJoinInteraction()

    @Nested
    inner class RestPPNamespaceBlockJoinInteraction : NamespaceBlockJoinInteraction()

    @Nested
    inner class RestPPUpdateChecks : UpdateChecks()

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
    }
}