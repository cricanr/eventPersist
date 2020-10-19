package main

import Module
import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import mqqt.MqttClientImpl
import mu.KLogger
import mu.KotlinLogging
import service.EventSubscriberService
import kotlin.system.exitProcess

private val logger: KLogger = KotlinLogging.logger {}

fun main() {
    val injector = Guice.createInjector(Module())
    val mqttClient = injector.getInstance<MqttClientImpl>()
    val eventSubscriberService = EventSubscriberService(mqttClient)
    logger.info("Welcome to the event persist MQTT client application!")

    eventSubscriberService.subscribe("event")

    println("You can stop any time the application by pressing key 'x'")
    val input = readLine()
    if (input == "x") {
        logger.error { "Application Terminating ..." }
        exitProcess(0)
    }
}

