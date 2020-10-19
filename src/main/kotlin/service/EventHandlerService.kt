package service

import com.google.inject.Inject
import models.toEntity
import models.toJson
import mu.KotlinLogging
import org.eclipse.paho.client.mqttv3.MqttMessage
import persist.IEventStore

interface IEventHandlerService {
    fun handleMessage(message: MqttMessage)
}

class EventHandlerService @Inject constructor(private val eventStore: IEventStore) : IEventHandlerService {
    private val logger = KotlinLogging.logger {}

    override fun handleMessage(message: MqttMessage) = run {
        try {
            val eventsPb = EventPBOuterClass.EventsPB.parseFrom(message.payload)
            logger.info("Received event converted from Google Protocol buffer: $eventsPb")

            val events = eventsPb.eventPBList.map { event -> event.toEntity() }
            val eventsAsJson = events.map { event -> event.toJson() }

            eventStore.persist(eventsAsJson)
        }
        catch (exception: Throwable) {
            logger.error("failed to handle message with id: ${message.id}. Message will be skipped")
        }
    }
}