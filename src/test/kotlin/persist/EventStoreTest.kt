package persist

import EventTestData.emptyEventsAsJson
import EventTestData.singleEventAsJson
import EventTestData.validEventsAsJson
import com.nhaarman.mockito_kotlin.*
import exceptions.ExceptionPersistingEventsToFile
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class EventStoreTest {
    private val filePersistMock = mock<FilePersist>()
    private val eventStore = EventStore(filePersistMock)

    @Test
    fun when_calling_persist_on_an_event_correctly_should_persist_event() {
        eventStore.persist(validEventsAsJson)

        validEventsAsJson.map { event ->
            doNothing().whenever(filePersistMock).writeLine(event)

            verify(filePersistMock, times(1)).writeLine(event)
        }
    }

    @Test
    fun when_calling_persist_with_no_event_correctly_should_not_persist() {
        val event = emptyEventsAsJson.first()
        doNothing().whenever(filePersistMock).writeLine(event)

        eventStore.persist(emptyEventsAsJson)
        verify(filePersistMock, never()).writeLine(event)
    }

    @Test
    fun when_calling_persist_and_a_failure_occurs_should_throw_an_exception() {
        val event = singleEventAsJson.first()

        whenever(filePersistMock.writeLine(event)).thenThrow(RuntimeException("file does not exist"))

        val failure = assertThrows<ExceptionPersistingEventsToFile> { eventStore.persist(singleEventAsJson) }

        assertEquals("Failure file does not exist occurred while persisting events to file", failure.message)
    }
}