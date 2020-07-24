package za.ac.sun.grapl.hooks

import org.apache.logging.log4j.LogManager
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.structure.Transaction
import java.io.File

class JanusGraphHook private constructor(builder: Builder) : GremlinHook(builder.graph) {

    private val logger = LogManager.getLogger(JanusGraphHook::class.java)
    private val supportsTransactions: Boolean
    private val conf: String
    private var tx: Transaction? = null

    override fun startTransaction() {
        if (supportsTransactions) {
            logger.debug("Supports tx")
            if (tx == null || !tx!!.isOpen) {
                logger.debug("Created new tx")
                try {
                    tx = AnonymousTraversalSource.traversal().withRemote(conf).tx()
                } catch (e: Exception) {
                    logger.error("Unable to create transaction!")
                }
            }
        } else {
            logger.debug("Does not support tx")
        }
        try {
            super.setTraversalSource(AnonymousTraversalSource.traversal().withRemote(conf))
        } catch (e: Exception) {
            logger.error("Unable to create transaction!")
        }
    }

    override fun endTransaction() {
        if (supportsTransactions) {
            var success = false
            var failures = 0
            val waitTime = 5000
            do {
                try {
                    if (tx == null) return
                    if (!tx!!.isOpen) return
                    tx!!.commit()
                    success = true
                } catch (e: IllegalStateException) {
                    failures++
                    if (failures > 3) {
                        logger.error("Failed to commit transaction $failures time(s). Aborting...")
                        return
                    } else {
                        logger.warn("Failed to commit transaction $failures time(s). Backing off and retrying...")
                        try {
                            Thread.sleep(waitTime.toLong())
                        } catch (ignored: Exception) {
                        }
                    }
                }
            } while (!success)
        } else {
            super.endTransaction()
        }
    }

    override fun exportCurrentGraph(exportDir: String) {
        require(isValidExportPath(exportDir)) {
            "Unsupported graph extension! Supported types are GraphML," +
                    " GraphSON, and Gryo."
        }
        val ext = exportDir.substring(exportDir.lastIndexOf('.') + 1).toLowerCase()
        startTransaction()
        when (ext) {
            "kryo", "json", "xml" -> throw UnsupportedOperationException("Export to Kryo, XML, and JSON currently not supported using the JanusGraphHook.")
        }
        endTransaction()
    }

    data class Builder(
            var conf: String,
            var graphDir: String? = null,
            var clearGraph: Boolean = false
    ) : GremlinHookBuilder {
        var graph: Graph? = null

        constructor(conf: String): this(conf, null, false)

        fun clearDatabase(clearGraph: Boolean) = apply { this.clearGraph = clearGraph }

        override fun useExistingGraph(graphDir: String): IHookBuilder {
            require(isValidExportPath(graphDir)) {
                "Unsupported graph extension! Supported types are GraphML," +
                        " GraphSON, and Gryo."
            }
            require(File(graphDir).exists()) { "No existing serialized graph file was found at $graphDir" }
            this.graphDir = graphDir
            return this
        }

        @Throws(Exception::class)
        override fun build(): JanusGraphHook {
            graph = AnonymousTraversalSource.traversal().withRemote(conf).graph
            return JanusGraphHook(this)
        }
    }

    init {
        conf = builder.conf
        if (builder.graphDir != null) graph.traversal().io<Any>(builder.graphDir).read().iterate()
        supportsTransactions = graph.features().graph().supportsTransactions()
        try {
            if (builder.clearGraph) clearGraph()
        } catch (e: Exception) {
            logger.warn("Unable to clear graph!", e)
        }
    }
}