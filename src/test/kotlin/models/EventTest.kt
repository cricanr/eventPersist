package models

import org.junit.Test
import kotlin.test.assertEquals

internal class EventTest {

    @Test
    fun when_casting_eventPB_to_event() {
        val eventPB = EventPBOuterClass.EventsPB.EventPB.getDefaultInstance()
        val event = eventPB.toEntity()

        assert(event.event == "")
        assert(event.timestamp == 0L)
        assert(event.userId == 0)
    }

    @Test
    fun when_calling_toJson_on_an_event_should_return_event_as_Json() {
        val event = Event(121L, 1, "my happy event").toJson()
        assertEquals("""{"timestamp":121,"userId":1,"event":"my happy event"}""", event)
    }
}