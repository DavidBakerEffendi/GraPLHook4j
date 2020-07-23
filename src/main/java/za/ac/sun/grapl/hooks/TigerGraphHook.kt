package za.ac.sun.grapl.hooks

import org.apache.logging.log4j.LogManager
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper
import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.mappers.VertexMapper
import za.ac.sun.grapl.domain.models.GraPLVertex
import za.ac.sun.grapl.domain.models.MethodDescriptorVertex
import za.ac.sun.grapl.domain.models.vertices.*
import java.io.IOException
import java.lang.Thread.sleep
import java.util.*


class TigerGraphHook private constructor(
        val hostname: String,
        val port: Int,
        val username: String,
        val password: String,
        val pathToCert: String?
) : IHook {

    val logger = LogManager.getLogger(TigerGraphHook::class.java)
    val api: String

    init {
        api = "http${if (pathToCert != null) "s" else ""}://$hostname:$port"
    }

    override fun exportCurrentGraph(exportDir: String) {
        TODO("Not yet implemented")
    }

    override fun joinFileVertexTo(to: FileVertex, from: NamespaceBlockVertex) {
        TODO("Not yet implemented")
    }

    override fun joinFileVertexTo(from: FileVertex, to: MethodVertex) {
        TODO("Not yet implemented")
    }

    override fun registerMetaData(vertex: MetaDataVertex) {
        TODO("Not yet implemented")
    }

    override fun createAndAddToMethod(from: MethodVertex, to: MethodDescriptorVertex) {
        TODO("Not yet implemented")
    }

    override fun createAndAddToMethod(from: MethodVertex, to: ModifierVertex) {
        TODO("Not yet implemented")
    }

    override fun joinASTVerticesByOrder(blockFrom: Int, blockTo: Int, edgeLabel: EdgeLabels) {
        TODO("Not yet implemented")
    }

    override fun areASTVerticesJoinedByEdge(blockFrom: Int, blockTo: Int, edgeLabel: EdgeLabels): Boolean {
        TODO("Not yet implemented")
    }

    override fun joinNamespaceBlocks(from: NamespaceBlockVertex, to: NamespaceBlockVertex) {
        TODO("Not yet implemented")
    }

    override fun maxOrder(): Int {
        TODO("Not yet implemented")
    }

    override fun updateASTVertexProperty(rootVertex: MethodVertex, order: Int, key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun updateASTVertexProperty(order: Int, key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun addFileVertex(v: FileVertex) {
        TODO("Not yet implemented")
    }

    override fun isASTVertex(blockOrder: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun createAndAssignToBlock(parentVertex: MethodVertex, newVertex: GraPLVertex) {
        TODO("Not yet implemented")
    }

    override fun createAndAssignToBlock(rootVertex: MethodVertex, newVertex: GraPLVertex, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    override fun createAndAssignToBlock(newVertex: GraPLVertex, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    override fun createVertex(graPLVertex: GraPLVertex) {
        val propertyMap = VertexMapper.propertiesToMap(graPLVertex)
        val vertexLabel = propertyMap.remove("label")
        val attributes = mutableMapOf<String, Any>()
        propertyMap.forEach {
            val key = if (it.key == "order") "astOrder" else it.key
            attributes[key] = mapOf("value" to it.value)
        }
        val payload = mutableMapOf<String, Any>(
                "vertices" to mapOf<String, Any>(
                        "${vertexLabel}_VERT" to mapOf<String, Any>(
                                graPLVertex.hashCode().toString() to attributes
                        )
                )
        )
        post("graph/$GRAPH_NAME", payload)
    }

    override fun close() {
        /* No need to close anything - this hook uses REST */
    }

    override fun clearGraph() =
            EnumSet.allOf(VertexLabels::class.java).forEach {
                delete("graph/$GRAPH_NAME/delete_by_type/vertices/${it.name}")
            }

    private fun post(endpoint: String, payload: Map<String, Any>) {
        var tryCount = 0
        val objectMapper = ObjectMapper()
        while (++tryCount < MAX_RETRY) {
            val response = khttp.post(
                    url ="$api/$endpoint",
                    headers = mapOf("Content-Type" to "application/json"),
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
            val response = khttp.delete("$api/$endpoint")
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
        private const val DEFAULT_USER = "tigergraph"
        private const val DEFAULT_PASSWORD = "tigergraph"
        private const val GRAPH_NAME = "cpg"
        private const val MAX_RETRY = 5
    }

    data class Builder(
            var hostname: String = DEFAULT_HOSTNAME,
            var port: Int = DEFAULT_PORT,
            var username: String = DEFAULT_USER,
            var password: String = DEFAULT_PASSWORD,
            var pathToCert: String?
    ) : IHookBuilder {

        constructor() : this(DEFAULT_HOSTNAME, DEFAULT_PORT, DEFAULT_USER, DEFAULT_PASSWORD, null)

        fun hostname(hostname: String) = apply { this.hostname = hostname }
        fun port(port: Int) = apply { this.port = port }
        fun username(username: String) = apply { this.username = username }
        fun password(password: String) = apply { this.password = password }
        fun pathToCert(pathToCert: String) = apply { this.pathToCert = pathToCert }

        override fun build(): TigerGraphHook = TigerGraphHook(hostname, port, username, password, pathToCert)

    }
}