package za.ac.sun.grapl.hooks

import za.ac.sun.grapl.domain.models.vertices.BlockVertex
import za.ac.sun.grapl.domain.models.vertices.ControlStructureVertex
import za.ac.sun.grapl.domain.models.vertices.FileVertex
import za.ac.sun.grapl.domain.models.vertices.LiteralVertex
import za.ac.sun.grapl.domain.models.vertices.LocalVertex
import za.ac.sun.grapl.domain.models.vertices.MethodParameterInVertex
import za.ac.sun.grapl.domain.models.vertices.MethodReturnVertex
import za.ac.sun.grapl.domain.models.vertices.MethodVertex
import za.ac.sun.grapl.domain.models.vertices.ModifierVertex
import za.ac.sun.grapl.domain.models.vertices.NamespaceBlockVertex

class TigerGraphHook private constructor(
//        builder: TigerGraphHookBuilder
) : IHook {

    val hostname: String? = "127.0.0.1"

    init {
        print("hey")
    }

    override fun isBlock(blockOrder: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun exportCurrentGraph(exportDir: String?) {
        TODO("Not yet implemented")
    }

    override fun joinFileVertexTo(to: FileVertex?, from: NamespaceBlockVertex?) {
        TODO("Not yet implemented")
    }

    override fun joinFileVertexTo(from: FileVertex?, to: MethodVertex?) {
        TODO("Not yet implemented")
    }

    override fun joinNamespaceBlocks(from: NamespaceBlockVertex?, to: NamespaceBlockVertex?) {
        TODO("Not yet implemented")
    }

    override fun maxOrder(): Int {
        TODO("Not yet implemented")
    }

    override fun addFileVertex(v: FileVertex?) {
        TODO("Not yet implemented")
    }

    override fun createAndAddToMethod(from: MethodVertex?, to: MethodParameterInVertex?) {
        TODO("Not yet implemented")
    }

    override fun createAndAddToMethod(from: MethodVertex?, to: MethodReturnVertex?) {
        TODO("Not yet implemented")
    }

    override fun createAndAddToMethod(from: MethodVertex?, to: ModifierVertex?) {
        TODO("Not yet implemented")
    }

    override fun assignToBlock(rootMethod: MethodVertex?, local: LocalVertex?, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    override fun assignToBlock(rootMethod: MethodVertex?, literal: LiteralVertex?, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    override fun assignToBlock(rootMethod: MethodVertex?, block: BlockVertex?, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    override fun assignToBlock(rootMethod: MethodVertex?, control: ControlStructureVertex?, blockOrder: Int) {
        TODO("Not yet implemented")
    }

    class TigerGraphHookBuilder {
        var createNewGraph = false
            private set
        var hostname: String? = "127.0.0.1"
            private set
        var port: Int? = 14240
            private set
        var username: String? = "tigergraph"
            private set
        var password: String? = "tigergraph"
            private set
        var graphName: String? = "MyGraph"
            private set

        fun createNewGraph(createNewGraph: Boolean) = apply { this.createNewGraph = createNewGraph }

        fun hostname(hostname: String) = apply { this.hostname = hostname }

        fun port(port: Int) = apply { this.port = port }

        fun username(username: String?) = apply { this.username = username }

        fun password(password: String?) = apply { this.password = password }

        fun graphName(graphName: String?) = apply { this.graphName = graphName }

        fun build() = TigerGraphHook()

    }

}