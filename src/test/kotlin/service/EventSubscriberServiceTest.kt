package service

import com.nhaarman.mockito_kotlin.*
import exceptions.ExceptionSubscribingToQueue
import mqqt.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class EventSubscriberServiceTest {
    private val mqttClientMock: IMqttClient = mock<IMqttClient>()
    private val myTopic = "rating"
    private val eventSubscriberService = EventSubscriberService(mqttClientMock)
    private val argCaptor: KArgumentCaptor<(String, MqttMessage) -> Unit> = argumentCaptor<(String, MqttMessage) -> Unit>()

    @Test
    fun when_subscribing_to_mqtt_topic_successfully_no_exception_should_be_thrown() {
        doNothing().whenever(mqttClientMock).subscribe(any(), any())

        assertDoesNotThrow { eventSubscriberService.subscribe(myTopic) }
        verify(mqttClientMock).subscribe(any(), argCaptor.capture())
    }

    @Test
    fun when_exception_occurs_at_subscribing_to_Mqtt_topic_failure_should_be_thrown() {
        doThrow(RuntimeException("subscribing failure"))
            .`when`(mqttClientMock)
            .subscribe(any(), argCaptor.capture())

        assertThrows<ExceptionSubscribingToQueue> { eventSubscriberService.subscribe(myTopic) }
    }

    @Test
    fun when_closing_mqtt_topic_successfully_no_exception_should_be_thrown() {
        doNothing().whenever(mqttClientMock).close()

        assertDoesNotThrow { eventSubscriberService.close() }
        verify(mqttClientMock).close()
    }
}