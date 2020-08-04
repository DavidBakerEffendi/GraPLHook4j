package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.Nested

class JanusGraphHookIntTest : GremlinHookTest() {

    override fun provideBuilder(): GremlinHookBuilder {
        return JanusGraphHook.Builder("src/test/resources/conf/remote.properties")
    }

    override fun provideHook(): GremlinHook {
        return try {
            provideBuilder().build() as GremlinHook
        } catch (e: Exception) {
            throw Exception("Unable to build JanusGraphHook!")
        }
    }

    @Nested
    inner class JanusGraphCheckMethodJoinInteraction : CheckMethodJoinInteraction()

    @Nested
    inner class JanusGraphFileJoinInteraction: FileJoinInteraction()

    @Nested
    inner class JanusGraphBlockJoinInteraction: BlockJoinInteraction()

    @Nested
    inner class JanusGraphNamespaceBlockJoinInteraction: NamespaceBlockJoinInteraction()

    @Nested
    inner class JanusGraphUpdateChecks: UpdateChecks()

    @Nested
    inner class JanusGraphAggregateQueries : AggregateQueries()

    @Nested
    inner class JanusGraphBooleanChecks : BooleanChecks()

    @Nested
    inner class JanusGraphASTManipulation : ASTManipulation()

}