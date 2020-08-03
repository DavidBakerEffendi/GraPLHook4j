package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import za.ac.sun.grapl.domain.enums.DispatchTypes
import za.ac.sun.grapl.domain.enums.EdgeLabels
import za.ac.sun.grapl.domain.enums.EvaluationStrategies
import za.ac.sun.grapl.domain.enums.ModifierTypes
import za.ac.sun.grapl.domain.models.vertices.*

/**
 * Builds the test environments with hook calls - implementing class is required to validate results
 * with concerned database.
 */
abstract class HookTest {

    open fun provideHook(): IHook? = provideBuilder().build()

    open fun provideBuilder(): IHookBuilder = TinkerGraphHook.Builder()

    @Nested
    @DisplayName("Join method vertex to method related vertices")
    open inner class CheckMethodJoinInteraction {
        protected lateinit var hook: IHook
        private lateinit var m: MethodVertex

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
            val f = FileVertex(STRING_1, INT_1)
            m = MethodVertex(STRING_1, STRING_1, STRING_1, INT_1, INT_1)
            hook.joinFileVertexTo(f, m)
        }

        @AfterEach
        open fun tearDown() = hook.clearGraph()

        @Test
        open fun joinMethodToMethodParamIn() =
                hook.createAndAddToMethod(m, MethodParameterInVertex(STRING_1, STRING_1, EVAL_1, STRING_1, INT_1, INT_1))

        @Test
        open fun joinMethodToMethodReturn() =
                hook.createAndAddToMethod(m, MethodReturnVertex(STRING_1, STRING_1, EVAL_1, INT_1, INT_1))


        @Test
        open fun joinMethodToModifier() =
                hook.createAndAddToMethod(m, ModifierVertex(MOD_1, INT_1))
    }

    @Nested
    @DisplayName("Join file vertex to file related vertices")
    open inner class FileJoinInteraction {
        protected lateinit var hook: IHook
        private lateinit var f: FileVertex

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
            f = FileVertex(STRING_1, INT_1)
            hook.createVertex(f)
        }

        @AfterEach
        open fun tearDown() = hook.clearGraph()

        @Test
        open fun testJoinFile2NamespaceBlock() =
                hook.joinFileVertexTo(f, NamespaceBlockVertex(STRING_1, STRING_1, INT_1))

        @Test
        open fun testJoinFile2Method() =
                hook.joinFileVertexTo(f, MethodVertex(STRING_1, STRING_1, STRING_1, INT_1, INT_1))
    }

    @Nested
    @DisplayName("Check block vertex join behaviour")
    open inner class BlockJoinInteraction {
        protected lateinit var hook: IHook
        private lateinit var m: MethodVertex

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
            val f = FileVertex(STRING_1, INT_1)
            m = MethodVertex(ROOT_METHOD, STRING_2, STRING_2, INT_1, INT_2)
            hook.joinFileVertexTo(f, m)
            hook.createAndAssignToBlock(m, BlockVertex(FIRST_BLOCK, INT_3, INT_1, STRING_1, INT_1))
        }

        @AfterEach
        open fun tearDown() = hook.clearGraph()

        @Test
        open fun testMethodJoinBlockTest() = Unit // Simply check that the setup is correct

        @Test
        open fun testBlockJoinBlockTest() = hook.createAndAssignToBlock(BlockVertex(TEST_ID, INT_1, INT_4, STRING_1, INT_1), INT_3)

        @Test
        open fun testAssignLiteralToBlock() = hook.createAndAssignToBlock(LiteralVertex(TEST_ID, INT_4, INT_1, STRING_1, INT_1), INT_3)

        @Test
        open fun testAssignLocalToBlock() = hook.createAndAssignToBlock(LocalVertex(STRING_1, TEST_ID, STRING_1, INT_1, INT_4), INT_3)

        @Test
        open fun testAssignControlToBlock() = hook.createAndAssignToBlock(ControlStructureVertex(TEST_ID, 2, INT_4, 1), INT_3)
    }

    @Nested
    @DisplayName("Check namespace block related behaviour")
    open inner class NamespaceBlockJoinInteraction {
        protected lateinit var hook: IHook
        private lateinit var root: NamespaceBlockVertex

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
            root = NamespaceBlockVertex(ROOT_PACKAGE, ROOT_PACKAGE, INT_1)
        }

        @AfterEach
        open fun tearDown() = hook.clearGraph()

        @Test
        open fun joinTwoNamespaceBlocks() {
            val n1 = NamespaceBlockVertex(SECOND_PACKAGE, "$ROOT_PACKAGE.$SECOND_PACKAGE", INT_2)
            hook.joinNamespaceBlocks(root, n1)
        }

        @Test
        open fun joinThreeNamespaceBlocks() {
            val n1 = NamespaceBlockVertex(SECOND_PACKAGE, "$ROOT_PACKAGE.$SECOND_PACKAGE", INT_2)
            val n2 = NamespaceBlockVertex(THIRD_PACKAGE, "$ROOT_PACKAGE.$THIRD_PACKAGE", INT_3)
            hook.joinNamespaceBlocks(root, n1)
            hook.joinNamespaceBlocks(n1, n2)
        }

        @Test
        open fun joinExistingConnection() {
            val n1 = NamespaceBlockVertex(SECOND_PACKAGE, "$ROOT_PACKAGE.$SECOND_PACKAGE", 1)
            hook.joinNamespaceBlocks(root, n1)
            hook.joinNamespaceBlocks(root, n1)
        }
    }

    @Nested
    @DisplayName("Aggregate queries")
    open inner class AggregateQueries {
        private lateinit var hook: IHook

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
        }

        @AfterEach
        open fun tearDown() = hook.clearGraph()

        @Test
        open fun testEmptyGraph() = assertEquals(0, hook.maxOrder())

        @Test
        open fun testNonEmptyGraphWithASTVertices() {
            val f = FileVertex(STRING_1, INT_1)
            val m = MethodVertex(STRING_1, STRING_1, STRING_1, INT_1, INT_2)
            hook.joinFileVertexTo(f, m)
            hook.createAndAssignToBlock(m, BlockVertex(STRING_1, INT_3, INT_1, STRING_1, INT_1))
            hook.createAndAssignToBlock(m, BlockVertex(STRING_2, INT_4, INT_1, STRING_1, INT_1))
            assertEquals(INT_4, hook.maxOrder())
        }
    }

    @Nested
    @DisplayName("Simple boolean checks")
    open inner class BooleanChecks {
        private lateinit var hook: IHook

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
        }

        @AfterEach
        open fun tearDown() = hook.clearGraph()

        @Test
        open fun testEmptyGraph() = assertFalse(this.hook.isASTVertex(INT_1))

        @Test
        open fun testNonEmptyGraphWithBlockVertices() {
            val f = FileVertex(STRING_1, INT_1)
            val m = MethodVertex(STRING_1, STRING_1, STRING_1, INT_1, INT_2)
            hook.joinFileVertexTo(f, m)
            hook.createAndAssignToBlock(m, BlockVertex(STRING_1, INT_3, INT_1, STRING_1, INT_1))
            hook.createAndAssignToBlock(m, BlockVertex(STRING_2, INT_4, INT_1, STRING_1, INT_1))
            assertTrue(hook.isASTVertex(INT_1))
            assertTrue(hook.isASTVertex(INT_2))
            assertTrue(hook.isASTVertex(INT_3))
            assertTrue(hook.isASTVertex(INT_4))
            assertFalse(hook.isASTVertex(INT_4 + 1))
        }

        @Test
        open fun testNonEmptyGraphWithoutBlockVertices() {
            val f = FileVertex(STRING_1, INT_1)
            val m = MethodVertex(STRING_1, STRING_1, STRING_1, INT_1, INT_2)
            hook.joinFileVertexTo(f, m)
            assertTrue(hook.isASTVertex(INT_1))
            assertTrue(hook.isASTVertex(INT_2))
            assertFalse(hook.isASTVertex(INT_3))
        }
    }

    @Nested
    @DisplayName("Update Checks")
    open inner class UpdateChecks {
        protected lateinit var hook: IHook
        private lateinit var f: FileVertex
        private lateinit var m: MethodVertex
        protected val keyToTest = "typeFullName"
        protected val initValue = "INTEGER"
        protected val updatedValue = "VOID"

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
            hook.clearGraph()
            f = FileVertex(STRING_1, INT_1)
            m = MethodVertex(STRING_1, STRING_1, STRING_1, INT_1, INT_2)
            assertNotEquals(initValue, updatedValue)
            hook.createVertex(BlockVertex(STRING_1, INT_3, INT_1, initValue, INT_2))
        }

        @Test
        open fun testEmptyGraph() = assertFalse(this.hook.isASTVertex(INT_1))

        @Test
        open fun testUpdateOnOneBlockProperty() = hook.updateASTVertexProperty(INT_3, keyToTest, updatedValue)
    }

    @Nested
    @DisplayName("AST vertex unstructured manipulation queries")
    open inner class ASTManipulation {
        private lateinit var hook: IHook
        private lateinit var bv1: BlockVertex
        private lateinit var bv2: BlockVertex

        @BeforeEach
        open fun setUp() {
            hook = provideHook() ?: throw NullPointerException("Could not create hook!")
            bv1 = BlockVertex(STRING_1, INT_1, INT_1, STRING_1, INT_1)
            bv2 = BlockVertex(STRING_1, INT_2, INT_1, STRING_1, INT_1)
        }

        @AfterEach
        open fun tearDown() = hook.clearGraph()

        @Test
        open fun testCreatingFreeBlock() {
            assertFalse(hook.isASTVertex(INT_1))
            hook.createVertex(bv1)
            assertTrue(hook.isASTVertex(INT_1))
        }

        @Test
        open fun testCreatingAndJoiningFreeBlocks() {
            hook.createVertex(bv1)
            hook.createVertex(bv2)
            assertFalse(hook.areASTVerticesJoinedByEdge(INT_1, INT_2, EdgeLabels.AST))
            assertFalse(hook.areASTVerticesJoinedByEdge(INT_2, INT_1, EdgeLabels.AST))
            hook.joinASTVerticesByOrder(INT_1, INT_2, EdgeLabels.AST)
            assertTrue(hook.areASTVerticesJoinedByEdge(INT_1, INT_2, EdgeLabels.AST))
            assertTrue(hook.areASTVerticesJoinedByEdge(INT_2, INT_1, EdgeLabels.AST))
        }
    }

    companion object {
        const val STRING_1 = "TEST1"
        const val STRING_2 = "TEST2"
        const val INT_1 = 0
        const val INT_2 = 1
        const val INT_3 = 2
        const val INT_4 = 3
        val DISPATCH_1 = DispatchTypes.DYNAMIC_DISPATCH
        val DISPATCH_2 = DispatchTypes.STATIC_DISPATCH
        val EVAL_1 = EvaluationStrategies.BY_REFERENCE
        val EVAL_2 = EvaluationStrategies.BY_SHARING
        val MOD_1 = ModifierTypes.ABSTRACT
        val MOD_2 = ModifierTypes.CONSTRUCTOR
        const val ROOT_METHOD = "root"
        const val FIRST_BLOCK = "firstBlock"
        const val TEST_ID = "test"
        const val ROOT_PACKAGE = "za"
        const val SECOND_PACKAGE = "ac"
        const val THIRD_PACKAGE = "sun"
    }

}