package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.TestDomainResources.Companion.FIRST_BLOCK
import za.ac.sun.grapl.TestDomainResources.Companion.MOD_1
import za.ac.sun.grapl.TestDomainResources.Companion.ROOT_PACKAGE
import za.ac.sun.grapl.TestDomainResources.Companion.SECOND_PACKAGE
import za.ac.sun.grapl.TestDomainResources.Companion.STRING_1
import za.ac.sun.grapl.TestDomainResources.Companion.TEST_ID
import za.ac.sun.grapl.TestDomainResources.Companion.THIRD_PACKAGE
import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.models.vertices.*

abstract class AbstractGremlinHookTest : AbstractHookTest() {
    /**
     * Provides a hook to a new database hook. Default is a new [TinkerGraphHook].
     *
     * @return a built hook.
     */
    override fun provideHook(): GremlinHook {
        return TinkerGraphHook.Builder().build()
    }

    /**
     * Provides a hook builder to continue to configure.
     * Default is a [za.ac.sun.grapl.hooks.TinkerGraphHook.Builder].
     *
     * @return an [IHookBuilder] to build with.
     */
    override fun provideBuilder(): GremlinHookBuilder {
        return TinkerGraphHook.Builder()
    }

    @Nested
    inner class GremlinCheckMethodJoinInteraction : CheckMethodJoinInteraction() {
        private lateinit var gremlinHook: GremlinHook

        @BeforeEach
        override fun setUp() {
            super.setUp()
            gremlinHook = hook as GremlinHook
        }

        @Test
        override fun joinMethodToMethodParamIn() {
            super.joinMethodToMethodParamIn()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name).hasNext())
            Assertions.assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name)
                    .out(EdgeLabels.AST.name)
                    .has(MethodParameterInVertex.LABEL.name, "name", STRING_1)
                    .hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun joinMethodToMethodReturn() {
            super.joinMethodToMethodReturn()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name).hasNext())
            Assertions.assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name)
                    .out(EdgeLabels.AST.name)
                    .has(MethodReturnVertex.LABEL.name, "name", STRING_1)
                    .hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun joinMethodToModifier() {
            super.joinMethodToModifier()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name).hasNext())
            Assertions.assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name)
                    .out(EdgeLabels.AST.name)
                    .has(ModifierVertex.LABEL.name, "name", MOD_1.name)
                    .hasNext())
            gremlinHook.endTransaction()
        }
    }

    @Nested
    inner class GremlinFileJoinInteraction : FileJoinInteraction() {
        lateinit var gremlinHook: GremlinHook

        @BeforeEach
        override fun setUp() {
            super.setUp()
            gremlinHook = hook as GremlinHook
        }

        @Test
        override fun testJoinFile2NamespaceBlock() {
            super.testJoinFile2NamespaceBlock()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name).hasNext())
            Assertions.assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name, "fullName", STRING_1)
                    .out(EdgeLabels.AST.name)
                    .hasLabel(FileVertex.LABEL.name)
                    .hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun testJoinFile2Method() {
            super.testJoinFile2Method()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name).hasNext())
            Assertions.assertTrue(gremlinHook.g.V().hasLabel(FileVertex.LABEL.name)
                    .out(EdgeLabels.AST.name)
                    .has(MethodVertex.LABEL.name, "fullName", STRING_1)
                    .hasNext())
            gremlinHook.endTransaction()
        }
    }

    @Nested
    inner class GremlinBlockJoinInteraction : BlockJoinInteraction() {
        lateinit var gremlinHook: GremlinHook

        @BeforeEach
        override fun setUp() {
            super.setUp()
            gremlinHook = hook as GremlinHook
        }

        @Test
        override fun testMethodJoinBlockTest() {
            super.testMethodJoinBlockTest()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name).hasNext())
            Assertions.assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name)
                    .out(EdgeLabels.AST.name)
                    .has(BlockVertex.LABEL.name, "name", FIRST_BLOCK)
                    .hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun testBlockJoinBlockTest() {
            super.testBlockJoinBlockTest()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name, "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name)
                    .has(BlockVertex.LABEL.name, "name", TEST_ID)
                    .hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun testAssignLiteralToBlock() {
            super.testAssignLiteralToBlock()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name, "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name)
                    .has(LiteralVertex.LABEL.name, "name", TEST_ID)
                    .hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun testAssignLocalToBlock() {
            super.testAssignLocalToBlock()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name, "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name)
                    .has(LocalVertex.LABEL.name, "name", TEST_ID)
                    .hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun testAssignControlToBlock() {
            super.testAssignControlToBlock()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name, "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name)
                    .has(ControlStructureVertex.LABEL.name, "name", TEST_ID)
                    .hasNext())
            gremlinHook.endTransaction()
        }
    }

    @Nested
    inner class GremlinNamespaceBlockJoinInteraction : NamespaceBlockJoinInteraction() {
        lateinit var gremlinHook: GremlinHook

        @BeforeEach
        override fun setUp() {
            super.setUp()
            gremlinHook = hook as GremlinHook
        }

        @Test
        override fun joinTwoNamespaceBlocks() {
            super.joinTwoNamespaceBlocks()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name, "name", ROOT_PACKAGE)
                    .out(EdgeLabels.AST.name)
                    .has(NamespaceBlockVertex.LABEL.name, "name", SECOND_PACKAGE).hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun joinThreeNamespaceBlocks() {
            super.joinThreeNamespaceBlocks()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name, "name", ROOT_PACKAGE)
                    .out(EdgeLabels.AST.name)
                    .has(NamespaceBlockVertex.LABEL.name, "name", SECOND_PACKAGE).hasNext())
            Assertions.assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name, "name", SECOND_PACKAGE)
                    .out(EdgeLabels.AST.name)
                    .has(NamespaceBlockVertex.LABEL.name, "name", THIRD_PACKAGE).hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun joinExistingConnection() {
            super.joinExistingConnection()
            gremlinHook.startTransaction()
            Assertions.assertEquals(1, gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name, "name", ROOT_PACKAGE)
                    .out(EdgeLabels.AST.name).count().next())
            gremlinHook.endTransaction()
        }
    }

    @Nested
    inner class GremlinUpdateChecks : UpdateChecks() {
        lateinit var gremlinHook: GremlinHook

        @BeforeEach
        override fun setUp() {
            super.setUp()
            gremlinHook = hook as GremlinHook
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().hasLabel(BlockVertex.LABEL.name).has(super.keyToTest, super.initValue).hasNext())
            gremlinHook.endTransaction()
        }

        @Test
        override fun testUpdateOnOneBlockProperty() {
            super.testUpdateOnOneBlockProperty()
            gremlinHook.startTransaction()
            Assertions.assertTrue(gremlinHook.g.V().hasLabel(BlockVertex.LABEL.name).has(super.keyToTest, super.updatedValue).hasNext())
            gremlinHook.endTransaction()
        }
    }
}