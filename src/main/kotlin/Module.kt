import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import mqqt.IMqttClient
import mqqt.MqttClientFactory
import mqqt.MqttClientFactoryImpl
import mqqt.MqttClientImpl
import persist.*
import service.EventHandlerService
import service.EventSubscriberService
import service.IEventHandlerService
import service.IEventSubscriberService

class Module : KotlinModule() {
    override fun configure() {
        bind<IFilePersist>().to<FilePersist>().`in`<Singleton>()
        bind<IEventStore>().to<EventStore>().`in`<Singleton>()
        bind<IEventHandlerService>().to<EventHandlerService>().`in`<Singleton>()
        bind<IEventSubscriberService>().to<EventSubscriberService>().`in`<Singleton>()
        bind<IMqttClient>().to<MqttClientImpl>().`in`<Singleton>()
        bind<FileFactory>().to<FileFactoryImpl>().`in`<Singleton>()
        bind<MqttClientFactory>().to<MqttClientFactoryImpl>().`in`<Singleton>()
    }
}