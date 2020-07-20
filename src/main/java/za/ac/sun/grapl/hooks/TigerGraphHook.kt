package za.ac.sun.grapl.hooks

import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.models.GraPLVertex
import za.ac.sun.grapl.domain.models.MethodDescriptorVertex
import za.ac.sun.grapl.domain.models.vertices.*

class TigerGraphHook private constructor(
        val hostname: String,
        val port: Int,
        val username: String,
        val password: String,
        val pathToCert: String?
) : IHook {

    override fun exportCurrentGraph(exportDir: String?) {
        TODO("Not yet implemented")
    }

    override fun joinFileVertexTo(to: FileVertex?, from: NamespaceBlockVertex?) {
        TODO("Not yet implemented")
    }

    override fun joinFileVertexTo(from: FileVertex?, to: MethodVertex?) {
        TODO("Not yet implemented")
    }

    override fun registerMetaData(vertex: MetaDataVertex?) {
        TODO("Not yet implemented")
    }

    override fun createAndAddToMethod(from: MethodVertex?, to: MethodDescriptorVertex?) {
        TODO("Not yet implemented")
    }

    override fun createAndAddToMethod(from: MethodVertex?, to: ModifierVertex?) {
        TODO("Not yet implemented")
    }

    override fun joinASTVerticesByOrder(blockFrom: Int, blockTo: Int, edgeLabel: EdgeLabels?) {
        TODO("Not yet implemented")
    }

    override fun areASTVerticesJoinedByEdge(blockFrom: Int, blockTo: Int, edgeLabel: EdgeLabels?): Boolean {
        TODO("Not yet implemented")
    }

    override fun joinNamespaceBlocks(from: NamespaceBlockVertex?, to: NamespaceBlockVertex?) {
        TODO("Not yet implemented")
    }

    override fun maxOrder(): Int {
        TODO("Not yet implemented")
    }

    override fun updateASTVertexProperty(rootVertex: MethodVertex?, order: Int, key: String?, value: String?) {
        TODO("Not yet implemented")
    }

    override fun updateASTVertexProperty(order: Int, key: String?, value: String?) {
        TODO("Not yet implemented")
    }

    override fun addFileVertex(v: FileVertex?) {
        TODO("Not yet implemented")
    }

    override fun isASTVertex(blockOrder: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun createAndAssignToBlock(parentVertex: MethodVertex?, newVertex: GraPLVertex?) {
        TODO("Not yet implemented")
    }

    override fun createAndAssignToBlock(rootVertex: MethodVertex?, newVertex: GraPLVertex?, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    override fun createAndAssignToBlock(newVertex: GraPLVertex?, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    override fun createVertex(graPLVertex: GraPLVertex?) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun clearGraph() {
        TODO("Not yet implemented")
    }

    companion object {
        private val DEFAULT_HOSTNAME = "127.0.0.1"
        private val DEFAULT_PORT = 14240
        private val DEFAULT_USER = "tigergraph"
        private val DEFAULT_PASSWORD = "tigergraph"
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

        override fun build(): IHook = TigerGraphHook(hostname, port, username, password, pathToCert)

    }
}