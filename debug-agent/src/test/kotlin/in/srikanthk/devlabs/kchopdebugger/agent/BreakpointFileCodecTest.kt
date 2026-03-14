package `in`.srikanthk.devlabs.kchopdebugger.agent

import kotlin.test.Test
import kotlin.test.assertEquals

class BreakpointFileCodecTest {

    @Test
    fun `round trips breakpoints with sorted lines`() {
        val breakpoints = linkedMapOf(
            "/tmp/project/src/test/java/demo.feature" to listOf(8, 2, 5),
            "/tmp/project/src/test/java/with spaces.feature" to listOf(10)
        )

        val decoded = BreakpointFileCodec.decode(BreakpointFileCodec.encode(breakpoints))

        assertEquals(listOf(2, 5, 8), decoded["/tmp/project/src/test/java/demo.feature"]?.toList())
        assertEquals(listOf(10), decoded["/tmp/project/src/test/java/with spaces.feature"]?.toList())
    }

    @Test
    fun `decodes empty payload`() {
        assertEquals(emptyMap<String, Set<Int>>(), BreakpointFileCodec.decode(""))
    }
}
