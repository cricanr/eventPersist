package service

import EventPBOuterClass
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import persist.IEventStore

internal class EventHandlerServiceTest {
    private val eventStoreMock = mock<IEventStore>()
    private val eventHandlerService = EventHandlerService(eventStoreMock)

    @Test
    fun when_the_event_handler_service_handles_message_successfully_it_should_cast_it_and_call_persist() {
        doNothing().whenever(eventStoreMock).persist(any())

        val mqttMessage = MqttMessage(EventPBOuterClass.EventsPB.EventPB.getDefaultInstance().toByteArray())
        assertDoesNotThrow { eventHandlerService.handleMessage(mqttMessage) }
    }

    @Test
    fun when_the_event_handler_service_handle_message_fail_it_should_not_throw_an_exception() {
        whenever(eventStoreMock.persist(any())).thenThrow(RuntimeException("failure handling message"))

        val mqttMessage = MqttMessage(EventPBOuterClass.EventsPB.EventPB.getDefaultInstance().toByteArray())
        assertDoesNotThrow { eventHandlerService.handleMessage(mqttMessage) }
    }
}