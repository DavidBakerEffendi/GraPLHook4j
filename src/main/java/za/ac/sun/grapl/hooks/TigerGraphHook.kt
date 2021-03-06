package za.ac.sun.grapl.hooks

import org.apache.logging.log4j.LogManager
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper
import org.json.JSONArray
import org.json.JSONObject
import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.mappers.VertexMapper
import za.ac.sun.grapl.domain.models.GraPLVertex
import za.ac.sun.grapl.domain.models.MethodDescriptorVertex
import za.ac.sun.grapl.domain.models.vertices.FileVertex
import za.ac.sun.grapl.domain.models.vertices.MethodVertex
import za.ac.sun.grapl.domain.models.vertices.ModifierVertex
import za.ac.sun.grapl.domain.models.vertices.NamespaceBlockVertex
import java.io.IOException
import java.lang.Thread.sleep
import java.util.*


class TigerGraphHook private constructor(
        hostname: String,
        port: Int,
        secure: Boolean,
        private val authKey: String?
) : IHook {

    private val logger = LogManager.getLogger(TigerGraphHook::class.java)
    private val api: String = "http${if (secure) "s" else ""}://$hostname:$port"
    private val objectMapper = ObjectMapper()

    override fun joinFileVertexTo(to: FileVertex, from: NamespaceBlockVertex) = upsertAndJoinVertices(from, to, EdgeLabels.AST)

    override fun joinFileVertexTo(from: FileVertex, to: MethodVertex) = upsertAndJoinVertices(from, to, EdgeLabels.AST)

    override fun createAndAddToMethod(from: MethodVertex, to: MethodDescriptorVertex) = upsertAndJoinVertices(from, to, EdgeLabels.AST)

    override fun createAndAddToMethod(from: MethodVertex, to: ModifierVertex) = upsertAndJoinVertices(from, to, EdgeLabels.AST)

    override fun joinASTVerticesByOrder(blockFrom: Int, blockTo: Int, edgeLabel: EdgeLabels) {
        val from = getVertexByASTOrder(blockFrom)
        val to = getVertexByASTOrder(blockTo)
        if (from != null && to != null) upsertAndJoinVertices(from, to, EdgeLabels.AST)
    }

    override fun areASTVerticesConnected(orderFrom: Int, orderTo: Int, edgeLabel: EdgeLabels): Boolean = ((get("query/$GRAPH_NAME/areASTVerticesJoinedByEdge?blockFrom=$orderFrom&blockTo=$orderTo&edgeLabel=${edgeLabel.name}")).first() as JSONObject)["result"] as Boolean

    override fun joinNamespaceBlocks(from: NamespaceBlockVertex, to: NamespaceBlockVertex) = upsertAndJoinVertices(from, to, EdgeLabels.AST)

    override fun maxOrder() = (get("query/$GRAPH_NAME/maxOrder").first() as JSONObject)["@@maxAstOrder"] as Int

    override fun updateASTVertexProperty(order: Int, key: String, value: String) {
        val result = (get("query/$GRAPH_NAME/findVertexByAstOrder?astOrder=$order").first() as JSONObject)["result"] as JSONArray
        if (result.length() > 0) {
            val vertexMap = result.first() as JSONObject
            val flatMap = flattenVertexResult(vertexMap)
            // Update key-value pair and reconstruct as GraPLVertex
            flatMap[key] = value
            val vertex = VertexMapper.mapToVertex(flatMap)
            // Upsert vertex
            createVertex(vertex, vertexMap["v_id"] as String)
        }
    }

    override fun isASTVertex(blockOrder: Int): Boolean = ((get("query/$GRAPH_NAME/isASTVertex?astOrder=$blockOrder")).first() as JSONObject)["result"] as Boolean

    override fun createAndAssignToBlock(parentVertex: MethodVertex, newVertex: GraPLVertex) = upsertAndJoinVertices(parentVertex, newVertex, EdgeLabels.AST)

    override fun createAndAssignToBlock(newVertex: GraPLVertex, blockOrder: Int) {
        val from = getVertexByASTOrder(blockOrder) ?: return
        upsertAndJoinVertices(from, newVertex, EdgeLabels.AST)
    }

    override fun createVertex(graPLVertex: GraPLVertex) {
        val payload = mutableMapOf<String, Any>(
                "vertices" to createVertexPayload(graPLVertex)
        )
        post("graph/$GRAPH_NAME", payload)
    }

    private fun createVertex(graPLVertex: GraPLVertex, id: String) {
        val payload = mutableMapOf<String, Any>(
                "vertices" to createVertexPayload(graPLVertex, id)
        )
        post("graph/$GRAPH_NAME", payload)
    }

    private fun upsertAndJoinVertices(from: GraPLVertex, to: GraPLVertex, edgeLabel: EdgeLabels) {
        val fromPayload = createVertexPayload(from)
        val toPayload = createVertexPayload(to)
        val vertexPayload = if (fromPayload.keys.first() == toPayload.keys.first()) mapOf(
                fromPayload.keys.first() to mapOf(
                        from.hashCode().toString() to (fromPayload.values.first() as Map<*, *>)[from.hashCode().toString()],
                        to.hashCode().toString() to (toPayload.values.first() as Map<*, *>)[to.hashCode().toString()]
                ))
        else mapOf(
                fromPayload.keys.first() to fromPayload.values.first(),
                toPayload.keys.first() to toPayload.values.first()
        )
        val payload = mutableMapOf(
                "vertices" to vertexPayload,
                "edges" to createEdgePayload(from, to, edgeLabel)
        )
        post("graph/$GRAPH_NAME", payload)
    }

    private fun createVertexPayload(graPLVertex: GraPLVertex): Map<String, Any> {
        val propertyMap = VertexMapper.propertiesToMap(graPLVertex)
        val vertexLabel = propertyMap.remove("label")
        return mapOf("${vertexLabel}_VERT" to mapOf<String, Any>(
                graPLVertex.hashCode().toString() to extractAttributesFromMap(propertyMap)
        ))
    }

    private fun createVertexPayload(graPLVertex: GraPLVertex, id: String): Map<String, Any> {
        val propertyMap = VertexMapper.propertiesToMap(graPLVertex)
        val vertexLabel = propertyMap.remove("label")
        return mapOf("${vertexLabel}_VERT" to mapOf<String, Any>(
                id to extractAttributesFromMap(propertyMap)
        ))
    }

    private fun extractAttributesFromMap(propertyMap: MutableMap<String, Any>): MutableMap<String, Any> {
        val attributes = mutableMapOf<String, Any>()
        propertyMap.forEach {
            val key = if (it.key == "order") "astOrder" else it.key
            attributes[key] = mapOf("value" to it.value)
        }
        return attributes
    }


    private fun createEdgePayload(from: GraPLVertex, to: GraPLVertex, edge: EdgeLabels): Map<String, Any> {
        val fromPayload = createVertexPayload(from)
        val toPayload = createVertexPayload(to)
        val fromLabel = fromPayload.keys.first()
        val toLabel = toPayload.keys.first()
        return mapOf(
                fromLabel to mapOf(
                        from.hashCode().toString() to mapOf<String, Any>(
                                "${fromLabel.removeSuffix("_VERT")}_${toLabel.removeSuffix("_VERT")}" to mapOf<String, Any>(
                                        toLabel to mapOf<String, Any>(
                                                to.hashCode().toString() to mapOf<String, Any>(
                                                        "name" to mapOf("value" to edge.name)
                                                )
                                        )
                                )

                        )
                )
        )
    }

    fun flattenVertexResult(o: JSONObject): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        val attributes = o["attributes"] as JSONObject
        map["label"] = (o["v_type"] as String).removeSuffix("_VERT")
        attributes.keys().forEach { k ->
            when (k.toString()) {
                "astOrder" -> map["order"] = attributes[k] as Int
                else -> map[k] = attributes[k]
            }
        }
        return map
    }

    private fun getVertexByASTOrder(order: Int): GraPLVertex? {
        val result = (get("query/$GRAPH_NAME/findVertexByAstOrder?astOrder=$order").first() as JSONObject)["result"] as JSONArray
        if (result.length() > 0) {
            val vertexMap = result.first() as JSONObject
            val flatMap = flattenVertexResult(vertexMap)
            // Update key-value pair and reconstruct as GraPLVertex
            return VertexMapper.mapToVertex(flatMap)
        }
        return null
    }

    override fun close() {
        /* No need to close anything - this hook uses REST */
    }

    override fun clearGraph() =
            EnumSet.allOf(VertexLabels::class.java).forEach {
                delete("graph/$GRAPH_NAME/delete_by_type/vertices/${it.name}_VERT")
            }

    private fun headers(): Map<String, String> = if (authKey == null) {
        mapOf("Content-Type" to "application/json")
    } else {
        mapOf("Content-Type" to "application/json", "Authorization" to "Bearer $authKey")
    }

    private fun get(endpoint: String): JSONArray {
        var tryCount = 0
        while (++tryCount < MAX_RETRY) {
            val response = khttp.get(
                    url = "$api/$endpoint",
                    headers = headers()
            )
            logger.debug("Get ${response.url}")
            logger.debug("Response ${response.text}")
            when {
                response.statusCode == 200 -> return response.jsonObject["results"] as JSONArray
                tryCount >= MAX_RETRY -> throw IOException("Could not complete get request due to status code ${response.statusCode} at $api/$endpoint")
                else -> sleep(500)
            }
        }
        return JSONArray()
    }

    private fun post(endpoint: String, payload: Map<String, Any>) {
        var tryCount = 0

        while (++tryCount < MAX_RETRY) {
            val response = khttp.post(
                    url = "$api/$endpoint",
                    headers = headers(),
                    data = objectMapper.writeValueAsString(payload)
            )
            logger.debug("Post ${response.url} ${response.request.data}")
            logger.debug("Response ${response.text}")
            when {
                response.statusCode == 200 -> return
                tryCount >= MAX_RETRY -> throw IOException("Could not complete post request due to status code ${response.statusCode} at $api/$endpoint")
                else -> sleep(500)
            }
        }
    }

    private fun delete(endpoint: String) {
        var tryCount = 0
        while (++tryCount < MAX_RETRY) {
            val response = khttp.delete(url = "$api/$endpoint", headers = headers())
            logger.debug("Delete ${response.url}")
            logger.debug("Response ${response.text}")
            when {
                response.statusCode == 200 -> return
                tryCount >= MAX_RETRY -> throw IOException("Could not complete delete request due to status code ${response.statusCode} at $api/$endpoint")
                else -> sleep(500)
            }
        }
    }

    companion object {
        private const val DEFAULT_HOSTNAME = "127.0.0.1"
        private const val DEFAULT_PORT = 9000
        private const val GRAPH_NAME = "cpg"
        private const val MAX_RETRY = 5
    }

    data class Builder(
            var hostname: String = DEFAULT_HOSTNAME,
            var port: Int = DEFAULT_PORT,
            var secure: Boolean = false,
            var authKey: String?
    ) : IHookBuilder {

        constructor() : this(DEFAULT_HOSTNAME, DEFAULT_PORT, false, null)

        fun hostname(hostname: String) = apply { this.hostname = hostname }
        fun port(port: Int) = apply { this.port = port }
        fun secure(secure: Boolean) = apply { this.secure = secure }
        fun authKey(authKey: String) = apply { this.authKey = authKey }

        override fun build(): TigerGraphHook = TigerGraphHook(hostname, port, secure, authKey)

    }
}