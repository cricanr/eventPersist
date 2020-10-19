package service

import com.google.inject.Inject
import exceptions.ExceptionSubscribingToQueue
import mqqt.IMqttClient
import mu.KotlinLogging

interface IEventSubscriberService {
    fun subscribe(topic: String)
    fun close()
}

class EventSubscriberService @Inject constructor(private val mqttClient: IMqttClient) : IEventSubscriberService {
    private val logger = KotlinLogging.logger {}

    override fun subscribe(topic: String) = run {
        try {
            mqttClient.subscribe(topic)
        } catch (exception: Throwable) {
            val message = "Failure subscribing to topic to receive events. Please try again later."
            logger.error(message)
            throw ExceptionSubscribingToQueue(message)
        }
    }

    override fun close() {
        mqttClient.close()
    }
}