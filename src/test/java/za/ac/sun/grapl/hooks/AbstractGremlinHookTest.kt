package za.ac.sun.grapl.hooks;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import za.ac.sun.grapl.domain.enums.EdgeLabels;
import za.ac.sun.grapl.domain.models.vertices.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class GremlinHookTest extends HookTest {

    /**
     * Provides a hook to a new database hook. Default is a new {@link TinkerGraphHook}.
     *
     * @return a built hook.
     */
    @NotNull
    @Override
    public GremlinHook provideHook() {
        return new TinkerGraphHook.Builder().build();
    }

    /**
     * Provides a hook builder to continue to configure.
     * Default is a {@link za.ac.sun.grapl.hooks.TinkerGraphHook.Builder}.
     *
     * @return an {@link IHookBuilder} to build with.
     */
    @NotNull
    @Override
    public GremlinHookBuilder provideBuilder() {
        return new TinkerGraphHook.Builder();
    }

    @Nested
    class GremlinCheckMethodJoinInteraction extends CheckMethodJoinInteraction {
        GremlinHook gremlinHook;

        @BeforeEach
        @Override
        public void setUp() {
            super.setUp();
            gremlinHook = (GremlinHook) hook;
        }

        @Test
        @Override
        public void joinMethodToMethodParamIn() {
            super.joinMethodToMethodParamIn();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name()).hasNext());
            assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name())
                    .out(EdgeLabels.AST.name())
                    .has(MethodParameterInVertex.LABEL.name(), "name", HookTest.STRING_1)
                    .hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void joinMethodToMethodReturn() {
            super.joinMethodToMethodReturn();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name()).hasNext());
            assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name())
                    .out(EdgeLabels.AST.name())
                    .has(MethodReturnVertex.LABEL.name(), "name", HookTest.STRING_1)
                    .hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void joinMethodToModifier() {
            super.joinMethodToModifier();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name()).hasNext());
            assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name())
                    .out(EdgeLabels.AST.name())
                    .has(ModifierVertex.LABEL.name(), "name", HookTest.Companion.getMOD_1().name())
                    .hasNext());
            gremlinHook.endTransaction();
        }
    }

    @Nested
    class GremlinFileJoinInteraction extends FileJoinInteraction {
        GremlinHook gremlinHook;

        @BeforeEach
        @Override
        public void setUp() {
            super.setUp();
            gremlinHook = (GremlinHook) hook;
        }

        @Test
        @Override
        public void testJoinFile2NamespaceBlock() {
            super.testJoinFile2NamespaceBlock();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name()).hasNext());
            assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name(), "fullName", HookTest.STRING_1)
                    .out(EdgeLabels.AST.name())
                    .hasLabel(FileVertex.LABEL.name())
                    .hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void testJoinFile2Method() {
            super.testJoinFile2Method();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name()).hasNext());
            assertTrue(gremlinHook.g.V().hasLabel(FileVertex.LABEL.name())
                    .out(EdgeLabels.AST.name())
                    .has(MethodVertex.LABEL.name(), "fullName", HookTest.STRING_1)
                    .hasNext());
            gremlinHook.endTransaction();
        }
    }

    @Nested
    class GremlinBlockJoinInteraction extends BlockJoinInteraction {
        GremlinHook gremlinHook;

        @BeforeEach
        @Override
        public void setUp() {
            super.setUp();
            gremlinHook = (GremlinHook) hook;
        }

        @Test
        @Override
        public void testMethodJoinBlockTest() {
            super.testMethodJoinBlockTest();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.E().hasLabel(EdgeLabels.AST.name()).hasNext());
            assertTrue(gremlinHook.g.V().hasLabel(MethodVertex.LABEL.name())
                    .out(EdgeLabels.AST.name())
                    .has(BlockVertex.LABEL.name(), "name", FIRST_BLOCK)
                    .hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void testBlockJoinBlockTest() {
            super.testBlockJoinBlockTest();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name())
                    .has(BlockVertex.LABEL.name(), "name", TEST_ID)
                    .hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void testAssignLiteralToBlock() {
            super.testAssignLiteralToBlock();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name())
                    .has(LiteralVertex.LABEL.name(), "name", TEST_ID)
                    .hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void testAssignLocalToBlock() {
            super.testAssignLocalToBlock();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name())
                    .has(LocalVertex.LABEL.name(), "name", TEST_ID)
                    .hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void testAssignControlToBlock() {
            super.testAssignControlToBlock();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().has(BlockVertex.LABEL.name(), "name", FIRST_BLOCK)
                    .out(EdgeLabels.AST.name())
                    .has(ControlStructureVertex.LABEL.name(), "name", TEST_ID)
                    .hasNext());
            gremlinHook.endTransaction();
        }
    }

    @Nested
    class GremlinNamespaceBlockJoinInteraction extends NamespaceBlockJoinInteraction {
        GremlinHook gremlinHook;

        @BeforeEach
        @Override
        public void setUp() {
            super.setUp();
            gremlinHook = (GremlinHook) hook;
        }

        @Test
        @Override
        public void joinTwoNamespaceBlocks() {
            super.joinTwoNamespaceBlocks();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name(), "name", ROOT_PACKAGE)
                    .out(EdgeLabels.AST.name())
                    .has(NamespaceBlockVertex.LABEL.name(), "name", SECOND_PACKAGE).hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void joinThreeNamespaceBlocks() {
            super.joinThreeNamespaceBlocks();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name(), "name", ROOT_PACKAGE)
                    .out(EdgeLabels.AST.name())
                    .has(NamespaceBlockVertex.LABEL.name(), "name", SECOND_PACKAGE).hasNext());
            assertTrue(gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name(), "name", SECOND_PACKAGE)
                    .out(EdgeLabels.AST.name())
                    .has(NamespaceBlockVertex.LABEL.name(), "name", THIRD_PACKAGE).hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void joinExistingConnection() {
            super.joinExistingConnection();
            gremlinHook.startTransaction();
            assertEquals(1, gremlinHook.g.V().has(NamespaceBlockVertex.LABEL.name(), "name", ROOT_PACKAGE)
                    .out(EdgeLabels.AST.name()).count().next());
            gremlinHook.endTransaction();
        }
    }

    @Nested
    @DisplayName("Update Checks")
    class GremlinUpsertChecks extends UpdateChecks {
        GremlinHook gremlinHook;

        @BeforeEach
        @Override
        public void setUp() {
            super.setUp();
            gremlinHook = (GremlinHook) hook;
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().hasLabel(BlockVertex.LABEL.name()).has(super.getKeyToTest(), super.getInitValue()).hasNext());
            gremlinHook.endTransaction();
        }

        @Test
        @Override
        public void testUpdateOnOneBlockProperty() {
            super.testUpdateOnOneBlockProperty();
            gremlinHook.startTransaction();
            assertTrue(gremlinHook.g.V().hasLabel(BlockVertex.LABEL.name()).has(super.getKeyToTest(), super.getUpdatedValue()).hasNext());
            gremlinHook.endTransaction();
        }
    }
}
