Application that receives events over MQTT queue and saves them in the file
=====
This is a Kotlin Backend console application. At startup it connects using an MQTT client
to the `eventReceive` Scala Play application and subscribes to the `event` topic. It will 
wait for incoming messages and deserialize them from Google Protocol buffer format and
then save them locally on the disk in a file called: `events.txt`.
As an example an event looks like: 
```{
   "timestamp" : 1518609008,
   "userId" : 1123,
   "event" : "2 hours of downtime occured due to the release of version 1.0.5 of the
   system"
   }
```

This service receives over MQTT messages from this service: https://github.com/cricanr/eventReceive 
Architecture & code notes: 
======
Communication over TCP/IP using MQTT is done over: `tcp://localhost:1883` on topic: `event`.
Application is using `gradle` as a build tool. 
Testing is done using JUnit, dependency injection using Google Guice.  
Logging is done using `logback-classic` & `kotlin-logging`. Currently I am just logging
locally on the disk and in the stdout, logs are not collected for further monitoring purposes.
Communication over MQTT is done using: `org.eclipse.paho.client.mqttv3` library.
For working with Google Protocol Buffer I am using: `protobuf-java` and gradle `protobuf-gradle-plugin`
plugin to generate needed data classes. Also in gradle I customized the output for running
`clean build` to also properly describe the tests output.

The event store implementation is very basic, it basically writes each event as 1 line in the 
`events.txt` file. It does not consider multi-threading nor other resiliency features.

Main entry point is the `Main` class which will kick start the process to subscribe to the 
events over MQTT using: `EventSubscriberService`. For handling an incoming message class:
`EventHandlerService` will have the logic fetch, validate, cast the event and call persist
on the `EventStore`. Which in turn will use a `FilePersist` class to actually save files to the disk.

Protocol communication over MQTT is abstracted away using: MqttClientImpl. To keep
things simple it allows: `subscribe` and `close` methods.
In order to stop the application you need to press 'x'


Further development:
====
As a minimal solution I have not considered for now some points that should be addressed: 
* docker container with the application
* resiliency features, e.g. `resilience4j` as `hystrix` went to maintenance mode now. Resiliency should be built in the 
  MQTT client implementation.
* events persistence is very limited, for a cluster implementation it would not do. We need to consider a better storage and concurrency should be safe.
* file `events.txt` should have a customizable path 
* logging should be extended and a logging mechanism to collect logs for further monitoring
* Add more testing, especially integration tests for the 2 applications
* Improve the MQTT architecture to be more flexible
* add configuration entries
* add CI/CD    


Running the project & prerequisites instructions
===
Prerequisites: 
I am using a Kotlin-Java MQTT client but we need to have a broker installed. I am using `mosquitto` broker.
In order to set it up, please run the commands based on your system needs:

1) MacOS:
Link: https://subscription.packtpub.com/book/application_development/9781787287815/1/ch01lvl1sec12/installing-a-mosquitto-broker-on-macos

```
      brew install mosquitto
      /usr/local/sbin/mosquitto -c /usr/local/etc/mosquitto/mosquitto.conf

```

Ubuntu: 
Link: https://www.vultr.com/docs/how-to-install-mosquitto-mqtt-broker-server-on-ubuntu-16-04
 
```sudo apt-get update
   sudo apt-get install mosquitto
```
NOTE: This should be done for convenience in another terminal tab.

Running the application:
As a build tool I am using `gradle` so we have the following commands using terminal:
```./gradlew clean build``` this will clean, compile test. 
To run using gradle use: 
```./gradlew run```

You can run the application by running the Main class from IntelliJ: `kotlin/main/Main.kt`
Tests can also be run using IntelliJ too.

Useful links:
======
1) Kotlin command line: https://kotlinlang.org/docs/tutorials/command-line.html
2) MQTT broker: http://mosquitto.org/blog/
3) MQTT client: https://www.eclipse.org/paho/clients/java/ 
4) Protocol Buffer for Java: https://github.com/protocolbuffers/protobuf/tree/master/java
5) Protocol Buffer plugin for gradle: https://github.com/google/protobuf-gradle-plugin
6) Google protocol buffers: https://developers.google.com/protocol-buffers
