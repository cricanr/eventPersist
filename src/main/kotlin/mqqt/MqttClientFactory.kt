package mqqt

import org.eclipse.paho.client.mqttv3.MqttClient

interface MqttClientFactory {
    fun create(clientId: String, serverUri: String): MqttClient
}

class MqttClientFactoryImpl : MqttClientFactory {
    override fun create(clientId: String, serverUri: String): MqttClient = MqttClient(serverUri, clientId)
}