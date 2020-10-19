package mqqt

import com.nhaarman.mockito_kotlin.*
import org.eclipse.paho.client.mqttv3.MqttClient
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import service.IEventHandlerService
import kotlin.test.assertEquals

internal class MqttClientImplTest {

    private val eventHandlerServiceMock = mock<IEventHandlerService>()
    private val mqttClientFactoryMock = mock<MqttClientFactory>()
    private val mqttClientMock = mock<MqttClient>()
    private val mqttClientImpl = MqttClientImpl(eventHandlerServiceMock, mqttClientFactoryMock)

    @Test
    fun when_the_mqtt_client_calls_subscribe_correctly_underlying_client_should_be_called() {
        whenever(mqttClientFactoryMock.create(any(), any())).thenReturn(mqttClientMock)

        doNothing().whenever(mqttClientMock).connect()
        doNothing().whenever(mqttClientMock).subscribe(any<String>())
        doNothing().whenever(mqttClientMock).setCallback(any())

        doNothing().whenever(eventHandlerServiceMock).handleMessage(any())

        assertDoesNotThrow { mqttClientImpl.subscribe("event", null) }

        verify(mqttClientMock).connect()
        verify(mqttClientMock).subscribe(any<String>())
        verify(mqttClientMock).setCallback(any())
    }

    @Test
    fun when_the_mqtt_client_calls_subscribe_fails_at_connect_next_underlying_client_calls_not_done() {
        whenever(mqttClientFactoryMock.create(any(), any())).thenReturn(mqttClientMock)

        doNothing().whenever(mqttClientMock).connect()
        whenever(mqttClientMock.connect()).thenThrow(RuntimeException("error at connect"))
        doNothing().whenever(mqttClientMock).setCallback(any())
        doNothing().whenever(eventHandlerServiceMock).handleMessage(any())

        val failure = assertThrows<RuntimeException> { mqttClientImpl.subscribe("event", null) }
        assertEquals(failure.message, "error at connect")

        verify(mqttClientMock).connect()
        verify(mqttClientMock, times(0)).subscribe(any<String>())
        verify(mqttClientMock, times(0)).setCallback(any())
    }

    @Test
    fun when_the_mqtt_client_calls_subscribe_fails_at_subscribe_next_underlying_client_calls_not_done() {
        whenever(mqttClientFactoryMock.create(any(), any())).thenReturn(mqttClientMock)

        doNothing().whenever(mqttClientMock).connect()
        whenever(mqttClientMock.subscribe(any<String>())).thenThrow(RuntimeException("error at subscribe"))
        doNothing().whenever(mqttClientMock).setCallback(any())

        doNothing().whenever(eventHandlerServiceMock).handleMessage(any())

        val failure = assertThrows<RuntimeException> { mqttClientImpl.subscribe("event", null) }
        assertEquals(failure.message, "error at subscribe")

        verify(mqttClientMock).connect()
        verify(mqttClientMock).subscribe(any<String>())
        verify(mqttClientMock, times(0)).setCallback(any())
    }

    @Test
    fun when_the_mqtt_client_calls_setCallback_fails_at_subscribe_next_underlying_client_calls_not_done() {
        whenever(mqttClientFactoryMock.create(any(), any())).thenReturn(mqttClientMock)

        doNothing().whenever(mqttClientMock).connect()
        doNothing().whenever(mqttClientMock).subscribe(any<String>())
        whenever(mqttClientMock.setCallback(any())).thenThrow(RuntimeException("error at setting callback"))

        doNothing().whenever(eventHandlerServiceMock).handleMessage(any())

        val failure = assertThrows<RuntimeException> { mqttClientImpl.subscribe("event", null) }

        assertEquals(failure.message, "error at setting callback")

        verify(mqttClientMock).connect()
        verify(mqttClientMock).subscribe(any<String>())
        verify(mqttClientMock).setCallback(any())
    }

    @Test
    fun when_calling_the_mqtt_client_close_method_the_underlying_client_should_call_close() {
        whenever(mqttClientFactoryMock.create(any(), any())).thenReturn(mqttClientMock)

        mqttClientImpl.close()

        verify(mqttClientMock).close()
    }
}