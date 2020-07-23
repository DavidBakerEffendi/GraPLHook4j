package za.ac.sun.grapl.hooks

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.models.vertices.ArrayInitializerVertex

class TigerGraphHookIntTest {

    @Test
    fun eeee() {
        hook?.createVertex(ArrayInitializerVertex(1))
    }

    companion object {
        private var hook: TigerGraphHook? = null

        @BeforeAll
        @JvmStatic
        @Throws(Exception::class)
        internal fun setUpAll() {
            // Setup schema and build test database
            hook = TigerGraphHook.Builder().build()
        }

        @AfterAll
        @JvmStatic
        internal fun tearDownAll() {
            hook?.clearGraph()
            hook?.close()
        }
    }

}