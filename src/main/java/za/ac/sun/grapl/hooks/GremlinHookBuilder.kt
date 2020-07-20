package za.ac.sun.grapl.hooks

interface GremlinHookBuilder : IHookBuilder {
    fun useExistingGraph(graphDir: String?): IHookBuilder?
}