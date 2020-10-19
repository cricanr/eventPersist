package mqqt

import com.google.inject.Inject
import mu.KotlinLogging
import org.eclipse.paho.client.mqttv3.*
import service.IEventHandlerService

interface IMqttClient {
    fun subscribe(
        topics: String,
        messageCallBack: ((topic: String, message: MqttMessage) -> Unit)? = null
    )

    fun close()
}

class MqttClientImpl @Inject() constructor(
    private val eventHandlerService: IEventHandlerService,
    private val mqttClientFactory: MqttClientFactory
) : IMqttClient {
    private val client by lazy {
        val clientId = MqttClient.generateClientId()
        val serverUri = "tcp://localhost:1883"

        mqttClientFactory.create(clientId, serverUri)
    }

    private val logger = KotlinLogging.logger {}


    override fun subscribe(
        topics: String,
        messageCallBack: ((topic: String, message: MqttMessage) -> Unit)?
    ) {
        try {
            logger.info("Connecting to topic: $topics ...")
            println()

            client.connect()
            client.subscribe(topics)
            client.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String) {
                }

                override fun connectionLost(cause: Throwable) {
                    logger.warn("Connection lost to topic, details: ${cause.message}")
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    logger.info("Message with id: ${message.id} arrived on topic: $topic")

                    eventHandlerService.handleMessage(message)

                    messageCallBack?.invoke(topic, message)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    logger.info("Message delivery completed for message with id: ${token.message.id}")
                }
            })
        } catch (e: MqttException) {
            logger.error { "Failure occurred, details: ${e.message}"}
        }
    }

    override fun close() {
        client.apply {
            close()
        }
    }
}